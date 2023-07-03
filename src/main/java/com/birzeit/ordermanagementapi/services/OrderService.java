package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.LocalDateTimeFormatter;
import com.birzeit.ordermanagementapi.dtos.*;
import com.birzeit.ordermanagementapi.entities.*;
import com.birzeit.ordermanagementapi.exception.BadRequestException;
import com.birzeit.ordermanagementapi.repositories.OrderRepository;
import com.birzeit.ordermanagementapi.repositories.ProductOrderRepository;
import com.birzeit.ordermanagementapi.repositories.UserCredentialsRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductOrderRepository productOrderRepository;

    private final CustomerService customerService;

    private final ProductService productService;

    private final StockService stockService;

    private final UserCredentialsRepository userCredentialsRepository;

    public OrderService(OrderRepository orderRepository, ProductOrderRepository productOrderRepository,
                        CustomerService customerService, ProductService productService,
                        StockService stockService, UserCredentialsRepository userCredentialsRepository) {
        this.orderRepository = orderRepository;
        this.productOrderRepository = productOrderRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.stockService = stockService;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(OrderService::dtoMapping)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(int orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()) {
            throw new NoSuchElementException("No order found with id = " + orderId);
        }
        return dtoMapping(order.get());
    }

    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( ! (authentication instanceof UsernamePasswordAuthenticationToken)) {
            return null;
        }

        // get authenticated user's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserCredentials userCredentials = userCredentialsRepository.findByUsername(userDetails.getUsername());
        Customer customer = customerService.getCustomerByUsername(userCredentials);
        Order order = Order
                .builder()
                .customer(customer)
                .orderedAt(LocalDateTime.now())
                .build();

        // save the order initially to obtain its id to use it as a composite key for ProductOrder entity
        order = orderRepository.save(order);

        List<ProductOrder> productOrders = new ArrayList<>();
        for(ProductOrderRequestDTO productOrderRequestDTO: orderRequestDTO.orders()) {
            Product product = productService.getProductById(productOrderRequestDTO.productId());

            try {
                stockService.takeProductsForOrder(product, productOrderRequestDTO.quantity());
            } catch (BadRequestException e) {
                orderRepository.delete(order);
                throw e;
            }

            ProductOrder productOrder = ProductOrder
                    .builder()
                    .id(new ProductOrder.ProductOrderId(product.getId(), order.getId()))
                    .product(product)
                    .order(order)
                    .quantity(productOrderRequestDTO.quantity())
                    .price(product.getPrice())
                    .vat(product.getVat())
                    .build();

            productOrderRepository.save(productOrder);
            productOrders.add(productOrder);
        }

        order.setProductOrders(productOrders);

        Order savedOrder = orderRepository.save(order);
        return dtoMapping(savedOrder);
    }

    public static OrderResponseDTO dtoMapping(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                CustomerService.dtoMapping(order.getCustomer()),
                LocalDateTimeFormatter.formatDateAndTime(order.getOrderedAt()),
                order.getProductOrders()
                        .stream()
                        .map(OrderService::dtoMapping)
                        .collect(Collectors.toList())
        );
    }

    public static ProductOrderResponseDTO dtoMapping(ProductOrder productOrder) {
        return new ProductOrderResponseDTO(
                productOrder.getProduct().getId(),
                productOrder.getQuantity(),
                productOrder.getPrice(),
                productOrder.getVat()
        );
    }
}

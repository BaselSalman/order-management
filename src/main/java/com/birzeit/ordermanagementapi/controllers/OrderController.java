package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.OrderRequestDTO;
import com.birzeit.ordermanagementapi.dtos.OrderResponseDTO;
import com.birzeit.ordermanagementapi.dtos.ProductRequestDTO;
import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public OrderResponseDTO getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('order:write')")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDTO dto) throws URISyntaxException {
        OrderResponseDTO order = orderService.addOrder(dto);
        return ResponseEntity.created(new URI("/orders/" + order.id())).body(order);
    }
}

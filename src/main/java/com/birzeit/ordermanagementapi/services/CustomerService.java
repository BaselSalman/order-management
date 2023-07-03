package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.LocalDateTimeFormatter;
import com.birzeit.ordermanagementapi.dtos.CustomerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.CustomerResponseDTO;
import com.birzeit.ordermanagementapi.dtos.OrderResponseDTO;
import com.birzeit.ordermanagementapi.entities.Customer;
import com.birzeit.ordermanagementapi.entities.UserCredentials;
import com.birzeit.ordermanagementapi.repositories.CustomerRepository;
import com.birzeit.ordermanagementapi.repositories.UserCredentialsRepository;
import com.birzeit.ordermanagementapi.security.ApplicationSecurityConfig;
import jakarta.validation.Valid;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserCredentialsRepository userCredentialsRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            UserCredentialsRepository userCredentialsRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(CustomerService::dtoMapping)
                .collect(Collectors.toList());
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public CustomerResponseDTO getCustomerResponseById(int id) {
        return dtoMapping(getCustomerById(id));
    }

    public Customer getCustomerById(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()) {
            throw new NoSuchElementException("No customer found with id = " + id);
        }
        return customer.get();
    }

    public Customer getCustomerByUsername(UserCredentials userCredentials) {
        Customer customer = customerRepository.findByUserCredentials(userCredentials);
        if(customer == null) {
            throw new NoSuchElementException("No customer found with username = " + userCredentials.getUsername());
        }
        return customer;
    }

    public CustomerResponseDTO addCustomer(CustomerRequestDTO dto) {
        Customer customer = dtoMapping(dto);
        customer.setUserCredentials(UserCredentials
                .builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .user(customer)
                .role("CUSTOMER")
                .build());
        return dtoMapping(customerRepository.save(customer));
    }

    public List<OrderResponseDTO> getCustomerOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            // get authenticated user's details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserCredentials userCredentials = userCredentialsRepository.findByUsername(userDetails.getUsername());
            Customer customer = customerRepository.findByUserCredentials(userCredentials);
            return customer.getOrders().stream().map(OrderService::dtoMapping).collect(Collectors.toList());
        }
        return null;
    }

    public Customer dtoMapping(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setBornAt(LocalDateTimeFormatter.makeDateFromString(dto.bornAt()));
        return customer;
    }

    public static CustomerResponseDTO dtoMapping(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                LocalDateTimeFormatter.formatDate(customer.getBornAt()),
                customer.getUserCredentials().getUsername()
        );
    }

}

package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.CustomerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.CustomerResponseDTO;
import com.birzeit.ordermanagementapi.dtos.LoginDTO;
import com.birzeit.ordermanagementapi.dtos.OrderResponseDTO;
import com.birzeit.ordermanagementapi.entities.Customer;
import com.birzeit.ordermanagementapi.services.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('customer:read')")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('customer:read')")
    @SecurityRequirement(name = "Bearer Authentication")
    public CustomerResponseDTO getCustomerById(@PathVariable int id) {
        return customerService.getCustomerResponseById(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerRequestDTO dto) throws URISyntaxException {
        CustomerResponseDTO customer = customerService.addCustomer(dto);
        return ResponseEntity.created(new URI("/customers/" + customer.id())).body(customer);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<OrderResponseDTO> getCustomerOrders() {
        return customerService.getCustomerOrders();
    }

}

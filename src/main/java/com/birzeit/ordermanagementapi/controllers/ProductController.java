package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.CustomerRequestDTO;
import com.birzeit.ordermanagementapi.dtos.CustomerResponseDTO;
import com.birzeit.ordermanagementapi.dtos.ProductRequestDTO;
import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product:read')")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('product:write')")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequestDTO dto) throws URISyntaxException {
        Product product = productService.addProduct(dto);
        return ResponseEntity.created(new URI("/products/" + product.getId())).body(product);
    }

    @PatchMapping("/{id}/stockable")
    @PreAuthorize("hasAuthority('product:write')")
    public Product updateProductStockability(
            @PathVariable(name = "id") int productId,
            @RequestParam(name = "stockable") boolean isStockable) {
        return productService.updateStockability(productId, isStockable);
    }
}

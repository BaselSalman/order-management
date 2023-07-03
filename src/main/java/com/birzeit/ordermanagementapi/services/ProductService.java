package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.dtos.ProductRequestDTO;
import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.exception.BadRequestException;
import com.birzeit.ordermanagementapi.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw new NoSuchElementException("No product found with id = " + id);
        }
        return product.get();
    }

    public Product addProduct(ProductRequestDTO dto) {
        return productRepository.save(dtoMapping(dto));
    }

    public Product updateStockability(int productId, boolean isStockable) {
        Product product = getProductById(productId);
        product.setStockable(isStockable);
        return productRepository.save(product);
    }

    public static Product dtoMapping(ProductRequestDTO dto) {
        return Product
                .builder()
                .slug(dto.slug())
                .name(dto.name())
                .reference(dto.reference())
                .price(dto.price())
                .vat(dto.vat())
                .stockable(false)
                .build();
    }

}

package com.birzeit.ordermanagementapi.controllers;

import com.birzeit.ordermanagementapi.dtos.ProductRequestDTO;
import com.birzeit.ordermanagementapi.dtos.StockRequestDTO;
import com.birzeit.ordermanagementapi.dtos.StockResponseDTO;
import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.entities.Stock;
import com.birzeit.ordermanagementapi.services.StockService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
@SecurityRequirement(name = "Bearer Authentication")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<StockResponseDTO> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public StockResponseDTO getStockById(@PathVariable int id) {
        return stockService.getStockById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('stock:write')")
    public ResponseEntity<?> addStock(@RequestBody StockRequestDTO dto) throws URISyntaxException {
        StockResponseDTO stock = stockService.addStock(dto);
        return ResponseEntity.created(new URI("/stocks/" + stock.id())).body(stock);
    }

}

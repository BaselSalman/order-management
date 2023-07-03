package com.birzeit.ordermanagementapi.services;

import com.birzeit.ordermanagementapi.LocalDateTimeFormatter;
import com.birzeit.ordermanagementapi.dtos.StockRequestDTO;
import com.birzeit.ordermanagementapi.dtos.StockResponseDTO;
import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.entities.Stock;
import com.birzeit.ordermanagementapi.exception.BadRequestException;
import com.birzeit.ordermanagementapi.repositories.StockRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;

    private final ProductService productService;

    public StockService(StockRepository stockRepository, ProductService productService) {
        this.stockRepository = stockRepository;
        this.productService = productService;
    }
    public List<StockResponseDTO> getAllStocks() {
        return stockRepository
                .findAll()
                .stream()
                .map(StockService::dtoMapping)
                .collect(Collectors.toList());
    }

    public StockResponseDTO getStockById(int id) {
        Optional<StockResponseDTO> stockResponseDTO = stockRepository.findById(id).map(StockService::dtoMapping);
        if(stockResponseDTO.isEmpty()) {
            throw new NoSuchElementException("No stock found with id = " + id);
        }
        return stockResponseDTO.get();
    }

    public StockResponseDTO addStock(StockRequestDTO dto) {
        Product product = productService.getProductById(dto.productId());
        if(!product.isStockable()) {
            throw new BadRequestException("Product with id = " + dto.productId() + " is not stockable");
        }
        return dtoMapping(stockRepository.save(dtoMapping(dto)));
    }

    public int getStocksCountForSpecificProduct(Product product) {
        return stockRepository.findHowManyStocksAvailableForProduct(product);
    }

    public Stock addProductsToStock(Stock stock, int quantity) {
        if(quantity <= 0)
            throw new BadRequestException("Quantity amount must be greater than zero");
        stock.setQuantity(stock.getQuantity() + quantity);
        stock.setUpdatedAt(LocalDateTime.now());
        return stockRepository.save(stock);
    }

//    public int removeProductsFromStock(Stock stock, int quantity) {
//        if(quantity <= 0)
//            throw new BadRequestException("Quantity amount must be greater than zero");
//        stock.setQuantity(stock.getQuantity() - quantity);
//        int shortageQuantity = 0;
//        if(stock.getQuantity() < 0) {
//            shortageQuantity = -stock.getQuantity();
//            stock.setQuantity(0);
//        }
//        stock.setUpdatedAt(LocalDateTime.now());
//        stockRepository.save(stock);
//        return shortageQuantity;
//    }

    public void takeProductsForOrder(Product product, int quantity) {
        boolean isCompleted = false;
        int lastStockChecked = 0;
        List<Stock> stocks = stockRepository.findByProduct(product);

        // sort order by updated date from past to present
        Collections.sort(stocks);
        for(Stock stock: stocks) {
            if(stock.getQuantity() >= quantity) {
                stock.setQuantity(stock.getQuantity() - quantity);
                stock.setUpdatedAt(LocalDateTime.now());
                lastStockChecked++;
                isCompleted = true;
                break;
            } else {
                quantity -= stock.getQuantity();
                stock.setQuantity(0);
                stock.setUpdatedAt(LocalDateTime.now());
                lastStockChecked++;
            }
        }
        if(isCompleted) {
            for (int i = 0; i < lastStockChecked; i++) {
                stockRepository.save(stocks.get(i));
            }
        } else {
            throw new BadRequestException("Not enough quantity available for product: " + product.getName());
        }
    }

    public static StockResponseDTO dtoMapping(Stock stock) {
        return new StockResponseDTO(
                stock.getId(),
                stock.getProduct(),
                stock.getQuantity(),
                LocalDateTimeFormatter.formatDateAndTime(stock.getUpdatedAt())
        );
    }

    private Stock dtoMapping(StockRequestDTO dto) {
        return Stock
                .builder()
                .product(productService.getProductById(dto.productId()))
                .quantity(dto.quantity())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}

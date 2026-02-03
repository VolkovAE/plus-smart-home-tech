package ru.practicum.smart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.dto.product.ProductQuantityDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.service.ShoppingStoreService;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shopping-store")
@Validated
public class ShoppingStoreController {
    private final ShoppingStoreService storeService;

    @Autowired
    public ShoppingStoreController(@Qualifier("SensorServiceImpl") ShoppingStoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public Page<ProductDto> getProductsByCategory(@RequestParam("category") ProductCategory category, Pageable pageable) {
        return storeService.getProductsByCategory(category, pageable);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable("productId") String productId) {
        return storeService.getProduct(productId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return storeService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return storeService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean deleteProduct(@RequestBody String productId) {
        return storeService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setQuantityState(@RequestBody ProductQuantityDto productQuantityDto) {
        return storeService.updateQuantity(productQuantityDto);
    }
}

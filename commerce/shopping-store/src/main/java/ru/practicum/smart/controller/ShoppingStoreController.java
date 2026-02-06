package ru.practicum.smart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.feign.ShoppingStoreClient;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.QuantityState;
import ru.practicum.smart.service.ShoppingStoreService;

import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@Slf4j
@RestController
@RequestMapping(path = PATH_SHOPPING_STORE)
@Validated
public class ShoppingStoreController implements ShoppingStoreClient {
    private final ShoppingStoreService storeService;

    @Autowired
    public ShoppingStoreController(@Qualifier("StoreServiceImpl") ShoppingStoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    @GetMapping
    public Page<ProductDto> getProductsByCategory(@RequestParam(PATH_SHOPPING_STORE_CATEGORY) ProductCategory category, Pageable pageable) {
        return storeService.getProductsByCategory(category, pageable);
    }

    @Override
    @GetMapping(PATH_SHOPPING_STORE_PRODUCT_ID)
    public ProductDto getProduct(@PathVariable UUID productId) {
        return storeService.getProduct(productId);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return storeService.createProduct(productDto);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return storeService.updateProduct(productDto);
    }

    @Override
    @PostMapping(PATH_SHOPPING_STORE_REMOVE)
    public Boolean deleteProduct(@RequestBody UUID productId) {
        return storeService.deleteProduct(productId);
    }

//    @Override
//    @PostMapping(PATH_SHOPPING_STORE_QUANTITY_STATE)
//    public Boolean setQuantityState(@RequestBody ProductQuantityDto productQuantityDto) {
//        return storeService.updateQuantity(productQuantityDto);
//    }

    @Override
    @PostMapping(PATH_SHOPPING_STORE_QUANTITY_STATE)
    public Boolean setQuantityState(@RequestParam UUID productId,
                                    @RequestParam QuantityState quantityState) {
        return storeService.updateQuantity(productId, quantityState);
    }
}

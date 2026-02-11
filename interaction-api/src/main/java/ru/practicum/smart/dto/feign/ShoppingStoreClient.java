package ru.practicum.smart.dto.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.QuantityState;

import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_SHOPPING_STORE, path = PATH_SHOPPING_STORE)
public interface ShoppingStoreClient {
    @GetMapping
    Page<ProductDto> getProductsByCategory(@RequestParam(PATH_SHOPPING_STORE_CATEGORY) ProductCategory category, Pageable pageable);

    @GetMapping(PATH_SHOPPING_STORE_PRODUCT_ID)
    ProductDto getProduct(@PathVariable UUID productId);

    @PutMapping
    ProductDto createProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping(PATH_SHOPPING_STORE_REMOVE)
    Boolean deleteProduct(@RequestBody UUID productId);

    @PostMapping(PATH_SHOPPING_STORE_QUANTITY_STATE)
    Boolean setQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);
}

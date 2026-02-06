package ru.practicum.smart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.QuantityState;

import java.util.UUID;

public interface ShoppingStoreService {
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto getProduct(UUID productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean deleteProduct(UUID productId);

    //Boolean updateQuantity(ProductQuantityDto productQuantityDto);
    Boolean updateQuantity(UUID productId, QuantityState quantityState);
}

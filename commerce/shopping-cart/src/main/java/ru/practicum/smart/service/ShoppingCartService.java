package ru.practicum.smart.service;

import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    CartDto addProducts(String username, Map<String, Integer> products);

    CartDto removeProducts(String username, List<String> productIds);

    CartDto changeQuantity(String username, NewQuantityProduct changeQuantity);

    CartDto getActiveCart(String username);

    Boolean deactivate(String username);
}

package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Qualifier("CartServiceImpl")
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Override
    public CartDto addProducts(String username, Map<String, Integer> products) {
        return null;
    }

    @Override
    public CartDto removeProducts(String username, List<String> productIds) {
        return null;
    }

    @Override
    public CartDto changeQuantity(String username, NewQuantityProduct changeQuantity) {
        return null;
    }

    @Override
    public CartDto getActiveCart(String username) {
        return null;
    }

    @Override
    public Boolean deactivate(String username) {
        return null;
    }
}

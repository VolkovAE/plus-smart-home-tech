package ru.practicum.smart.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.model.Cart;
import ru.practicum.smart.model.CartProducts;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartDto toCartDto(Cart cart) {
        Map<String, Integer> products = cart.getProducts().stream()
                .collect(Collectors.toMap(
                        CartProducts::getProductId,
                        CartProducts::getQuantity
                ));

        return new CartDto(cart.getCartId(), products);
    }
}

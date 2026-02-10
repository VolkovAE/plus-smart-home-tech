package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;
import ru.practicum.smart.dto.feign.ShoppingCartClient;
import ru.practicum.smart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@Slf4j
@RestController
@RequestMapping(path = PATH_SHOPPING_CART)
@Validated
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService cartService;

    @Autowired
    public ShoppingCartController(@Qualifier("CartServiceImpl") ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @PutMapping
    public CartDto addProducts(@RequestParam String username,
                               @RequestBody Map<UUID, Integer> products) {
        return cartService.addProducts(username, products);
    }

    @Override
    @PostMapping(PATH_SHOPPING_CART_REMOVE)
    public CartDto removeProducts(@RequestParam String username,
                                  @RequestBody List<UUID> productIds) {
        return cartService.removeProducts(username, productIds);
    }

    @Override
    @PostMapping(PATH_SHOPPING_CHANGE_QUANTITY)
    public CartDto changeQuantity(@RequestParam String username,
                                  @Valid @RequestBody NewQuantityProduct changeQuantity) {
        return cartService.changeQuantity(username, changeQuantity);
    }

    @Override
    @GetMapping
    public CartDto getActiveCart(@RequestParam String username) {
        return cartService.getActiveCart(username);
    }

    @Override
    @DeleteMapping
    public Boolean deactivate(@RequestParam String username) {
        return cartService.deactivate(username);
    }
}

package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;
import ru.practicum.smart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@Validated
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @Autowired
    public ShoppingCartController(@Qualifier("CartServiceImpl") ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping
    public CartDto addProducts(@RequestParam String username,
                               @RequestBody Map<String, Integer> products) {
        return cartService.addProducts(username, products);
    }

    @PostMapping("/remove")
    public CartDto removeProducts(@RequestParam String username,
                                  @RequestBody List<String> productIds) {
        return cartService.removeProducts(username, productIds);
    }

    @PostMapping("/change-quantity")
    public CartDto changeQuantity(@RequestParam String username,
                                  @Valid @RequestBody NewQuantityProduct changeQuantity) {
        return cartService.changeQuantity(username, changeQuantity);
    }

    @GetMapping
    public CartDto getActiveCart(@RequestParam String username) {
        return cartService.getActiveCart(username);
    }

    @DeleteMapping
    public Boolean deactivate(@RequestParam String username) {
        return cartService.deactivate(username);
    }
}

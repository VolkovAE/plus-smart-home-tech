package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

import static ru.practicum.smart.util.StringConstants.MESSAGE_IF_NOT_NAME_USER;

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
    public CartDto removeProducts(@RequestParam
                                  @NotBlank(message = MESSAGE_IF_NOT_NAME_USER)
                                  @NotNull(message = MESSAGE_IF_NOT_NAME_USER) String username,
                                  @RequestBody List<String> productIds) {
        return cartService.removeProducts(username, productIds);
    }

    @PostMapping("/change-quantity")
    public CartDto changeQuantity(@RequestParam
                                  @NotBlank(message = MESSAGE_IF_NOT_NAME_USER)
                                  @NotNull(message = MESSAGE_IF_NOT_NAME_USER) String username,
                                  @Valid @RequestBody NewQuantityProduct changeQuantity) {
        return cartService.changeQuantity(username, changeQuantity);
    }

    @GetMapping
    public CartDto getActiveCart(@RequestParam
                                 @NotBlank(message = MESSAGE_IF_NOT_NAME_USER)
                                 @NotNull(message = MESSAGE_IF_NOT_NAME_USER) String username) {
        return cartService.getActiveCart(username);
    }

    @DeleteMapping
    public Boolean deactivate(@RequestParam
                              @NotBlank(message = MESSAGE_IF_NOT_NAME_USER)
                              @NotNull(message = MESSAGE_IF_NOT_NAME_USER) String username) {
        return cartService.deactivate(username);
    }
}

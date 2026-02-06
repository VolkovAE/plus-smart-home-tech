package ru.practicum.smart.dto.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;

import java.util.List;
import java.util.Map;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_SHOPPING_CART, path = PATH_SHOPPING_CART)
public interface ShoppingCartClient {
    @PutMapping
    CartDto addProducts(@RequestParam String username,
                               @RequestBody Map<String, Integer> products);

    @PostMapping(PATH_SHOPPING_CART_REMOVE)
    CartDto removeProducts(@RequestParam String username,
                                  @RequestBody List<String> productIds);

    @PostMapping(PATH_SHOPPING_CHANGE_QUANTITY)
    CartDto changeQuantity(@RequestParam String username,
                                  @Valid @RequestBody NewQuantityProduct changeQuantity);

    @GetMapping
    CartDto getActiveCart(@RequestParam String username);

    @DeleteMapping
    Boolean deactivate(@RequestParam String username);
}

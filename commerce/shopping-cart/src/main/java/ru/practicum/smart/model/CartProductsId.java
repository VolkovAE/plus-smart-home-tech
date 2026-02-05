package ru.practicum.smart.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@EqualsAndHashCode(of = {"cart", "productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProductsId implements Serializable {
    Cart cart;
    String productId;

    public CartProductsId() {
    }

    public CartProductsId(Cart cart, String productId) {
        this();
        this.cart = cart;
        this.productId = productId;
    }
}

package ru.practicum.smart.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(of = {"cart", "productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProductsId implements Serializable {
    Cart cart;
    UUID productId;

    public CartProductsId() {
    }

    public CartProductsId(Cart cart, UUID productId) {
        this();
        this.cart = cart;
        this.productId = productId;
    }
}

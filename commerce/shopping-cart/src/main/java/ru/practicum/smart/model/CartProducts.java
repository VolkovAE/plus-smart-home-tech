package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_CART_PRODUCTS, schema = SHEMA_NAME)
@IdClass(CartProductsId.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"cart", "productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProducts {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_NAME_ENTITY_CART_ID, nullable = false)
    Cart cart;

    @Id
    @Column(name = COLUMN_NAME_ENTITY_CART_PRODUCT_PRODUCT_ID, nullable = false)
    String productId;

    @Column(nullable = false)
    Integer quantity;
}

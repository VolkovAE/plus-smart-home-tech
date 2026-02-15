package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_CART, schema = SHEMA_NAME)
@Getter
@Setter
@EqualsAndHashCode(of = {"cartId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_NAME_ENTITY_CART_ID)
    UUID cartId;

    @Column(name = COLUMN_NAME_ENTITY_CART_USERNAME, nullable = false)
    String username;

    @Column(name = COLUMN_NAME_ENTITY_CART_ACTIVE, nullable = false)
    boolean active = true;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = CartProducts.class)
    List<CartProducts> products = new ArrayList<>();
}

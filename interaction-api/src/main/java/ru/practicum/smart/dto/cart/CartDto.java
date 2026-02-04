package ru.practicum.smart.dto.cart;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"cartId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {
    UUID cartId;

    Map<String, Integer> products;
}

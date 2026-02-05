package ru.practicum.smart.dto.cart;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"cartId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CartDto {
    @JsonProperty(value = "cartId")
    @JsonAlias(value = {"shoppingCartId"})
    UUID cartId;

    Map<String, Integer> products;
}

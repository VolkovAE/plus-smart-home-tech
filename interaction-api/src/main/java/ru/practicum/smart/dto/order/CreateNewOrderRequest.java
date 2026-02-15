package ru.practicum.smart.dto.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.warehouse.AddressDto;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull
    CartDto shoppingCart;

    @NotNull
    AddressDto deliveryAddress;
}

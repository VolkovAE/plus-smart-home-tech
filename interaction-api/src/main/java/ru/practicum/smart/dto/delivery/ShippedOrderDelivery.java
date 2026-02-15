package ru.practicum.smart.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ShippedOrderDelivery {
    @NotNull
    UUID orderId;

    @NotNull
    UUID deliveryId;
}

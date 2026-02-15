package ru.practicum.smart.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductsForOrderRequest {
    @NotNull
    Map<UUID, Integer> products;

    @NotNull
    UUID orderId;
}

package ru.practicum.smart.dto.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequest {
    @NotNull
    UUID orderId;

    @NotNull
    private Map<UUID, Integer> products;
}

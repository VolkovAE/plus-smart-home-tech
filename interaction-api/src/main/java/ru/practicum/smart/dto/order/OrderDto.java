package ru.practicum.smart.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import ru.practicum.smart.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID orderId;

    @Nullable
    UUID shoppingCartId;

    @NotNull
    Map<UUID, Integer> products;

    UUID paymentId;

    UUID deliveryId;

    OrderState orderState;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;
}

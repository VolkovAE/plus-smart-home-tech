package ru.practicum.smart.dto.delivery;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.enums.delivery.DeliveryState;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"deliveryId"})
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    UUID deliveryId;

    AddressDto fromAddress;

    AddressDto toAddress;

    UUID orderId;

    DeliveryState deliveryState;
}

package ru.practicum.smart.service;

import ru.practicum.smart.dto.delivery.DeliveryDto;
import ru.practicum.smart.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void deliverySuccessful(UUID orderId);

    void deliveryPicked(UUID orderId);

    void deliveryFailed(UUID orderId);

    BigDecimal getDeliveryCost(OrderDto orderDto);
}

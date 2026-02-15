package ru.practicum.smart.service;

import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto makePayment(OrderDto orderDto);

    BigDecimal getTotalCost(OrderDto orderDto);

    void paymentSuccess(UUID paymentId);

    BigDecimal getProductCost(OrderDto orderDto);

    void paymentFailed(UUID paymentId);
}

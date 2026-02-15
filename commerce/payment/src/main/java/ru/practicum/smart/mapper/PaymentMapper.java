package ru.practicum.smart.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.smart.dto.payment.PaymentDto;
import ru.practicum.smart.model.Payment;

@Component
public class PaymentMapper {
    public PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}

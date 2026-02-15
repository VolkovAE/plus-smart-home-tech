package ru.practicum.smart.dto.payment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"paymentId"})
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    @NotNull
    UUID paymentId;

    BigDecimal totalPayment;    // общая стоимость

    BigDecimal deliveryTotal;   //стоимость доставки

    BigDecimal feeTotal;    // стоимость налога
}

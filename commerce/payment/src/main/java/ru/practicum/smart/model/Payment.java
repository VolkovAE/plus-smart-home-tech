package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.enums.payment.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_PAYMENT, schema = SHEMA_NAME)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_ID)
    UUID paymentId;

    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_ORDER_ID)
    UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_STATE, nullable = false)
    PaymentState state;

    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_TOTAL_PAYMENT, precision = 15, scale = 2)
    BigDecimal totalPayment;

    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_DELIVERY_PAYMENT, precision = 15, scale = 2)
    BigDecimal deliveryTotal;

    @Column(name = COLUMN_NAME_ENTITY_PAYMENT_FEE_TOTAL, precision = 15, scale = 2)
    BigDecimal feeTotal;
}

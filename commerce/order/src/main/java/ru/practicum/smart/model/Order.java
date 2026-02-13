package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_ORDER, schema = SHEMA_NAME)
@Builder(toBuilder = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_NAME_ENTITY_ORDER_ID)
    UUID orderId;

    @Column(name = COLUMN_NAME_CART_ID, nullable = false)
    UUID shoppingCartId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = OrderItem.class)
    List<OrderItem> products = new ArrayList<>();

    @Column(name = COLUMN_NAME_PAYMENT_ID)
    UUID paymentId;

    @Column(name = COLUMN_NAME_DELIVERY_ID)
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_NAME_ORDER_STATET, nullable = false)
    OrderState orderState;

    @Column(name = COLUMN_NAME_DELIVERY_WEIGHT, nullable = false)
    Double deliveryWeight;

    @Column(name = COLUMN_NAME_DELIVERY_VOLUME, nullable = false)
    Double deliveryVolume;

    @Column(name = COLUMN_NAME_FRAGILE, nullable = false)
    Boolean fragile;

    @Column(name = COLUMN_NAME_TOTAL_PRICE, nullable = false)
    BigDecimal totalPrice;

    @Column(name = COLUMN_NAME_DELIVERY_PRICE, nullable = false)
    BigDecimal deliveryPrice;

    @Column(name = COLUMN_NAME_PRODUCT_PRICE, nullable = false)
    BigDecimal productPrice;

    @Column(name = COLUMN_NAME_USER_NAME)
    String userName;
}

package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.enums.delivery.DeliveryState;

import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_DELIVERY, schema = SHEMA_NAME)
@Getter
@Setter
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = COLUMN_NAME_ENTITY_DELIVERY_ID, nullable = false)
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_NAME_ENTITY_DELIVERY_STATE, nullable = false)
    private DeliveryState deliveryState;

    @Column(name = COLUMN_NAME_ENTITY_DELIVERY_ORDER_ID, nullable = false)
    private UUID orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = COLUMN_NAME_COUNTRY, column = @Column(name = TEXT_FROM + COLUMN_NAME_COUNTRY)),
            @AttributeOverride(name = COLUMN_NAME_CITY, column = @Column(name = TEXT_FROM + COLUMN_NAME_CITY)),
            @AttributeOverride(name = COLUMN_NAME_STREET, column = @Column(name = TEXT_FROM + COLUMN_NAME_STREET)),
            @AttributeOverride(name = COLUMN_NAME_HOUSE, column = @Column(name = TEXT_FROM + COLUMN_NAME_HOUSE)),
            @AttributeOverride(name = COLUMN_NAME_FLAT, column = @Column(name = TEXT_FROM + COLUMN_NAME_FLAT))
    })
    private Address fromAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = COLUMN_NAME_COUNTRY, column = @Column(name = TEXT_TO + COLUMN_NAME_COUNTRY)),
            @AttributeOverride(name = COLUMN_NAME_CITY, column = @Column(name = TEXT_TO + COLUMN_NAME_CITY)),
            @AttributeOverride(name = COLUMN_NAME_STREET, column = @Column(name = TEXT_TO + COLUMN_NAME_STREET)),
            @AttributeOverride(name = COLUMN_NAME_HOUSE, column = @Column(name = TEXT_TO + COLUMN_NAME_HOUSE)),
            @AttributeOverride(name = COLUMN_NAME_FLAT, column = @Column(name = TEXT_TO + COLUMN_NAME_FLAT))
    })
    private Address toAddress;
}

package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_BOOKING_PRODUCTS, schema = SHEMA_NAME, uniqueConstraints = @UniqueConstraint(columnNames = {COLUMN_NAME_BOOKING_ID, COLUMN_NAME_ENTITY_PRODUCT_ID}))
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_NAME_BOOKING_ID, nullable = false)
    OrderBooking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_NAME_ENTITY_PRODUCT_ID, nullable = false)
    QuantityProductInWarehouse product;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_QUANTITY, nullable = false)
    Integer quantity;
}

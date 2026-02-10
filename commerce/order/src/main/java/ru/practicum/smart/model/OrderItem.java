package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_ORDER_ITEMS, schema = SHEMA_NAME)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_NAME_ORDER_ID, nullable = false)
    private Order order;

    @Column(name = COLUMN_NAME_PRODUCT_ID, nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;
}

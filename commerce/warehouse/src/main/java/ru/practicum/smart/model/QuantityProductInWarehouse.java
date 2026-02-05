package ru.practicum.smart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static ru.practicum.smart.util.StringConstants.*;

@Entity
@Table(name = TABLE_NAME_ENTITY_PRODUCT, schema = SHEMA_NAME)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuantityProductInWarehouse {
    @Id
    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_ID, nullable = false)
    String productId;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_DEPTH, precision = 15, scale = 2)
    BigDecimal depth;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_HEIGHT, precision = 15, scale = 2)
    BigDecimal height;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_WIDTH, precision = 15, scale = 2)
    BigDecimal width;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_QUANTITY, nullable = false)
    Integer quantity = 1;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_WEIGHT, precision = 15, scale = 3)
    BigDecimal weight;

    @Column(name = COLUMN_NAME_ENTITY_PRODUCT_FRAGILE)
    Boolean fragile = false;
}

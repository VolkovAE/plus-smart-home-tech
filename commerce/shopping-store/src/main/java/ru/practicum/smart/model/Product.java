package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.ProductState;
import ru.practicum.smart.enums.product.QuantityState;

import java.math.BigDecimal;

import static ru.practicum.smart.util.StringConstants.SHEMA_NAME;
import static ru.practicum.smart.util.StringConstants.TABLE_NAME_ENTITY_PRODUCT;

@Entity
@Table(name = TABLE_NAME_ENTITY_PRODUCT, schema = SHEMA_NAME)
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "product_Id")
    String productId;

    @Column(nullable = false, name = "product_Name")
    String productName;

    @Column(nullable = false)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "quantity_State")
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "product_State")
    ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "product_Category")
    ProductCategory productCategory;

    @Column(nullable = false)
    BigDecimal price;

    @Column(name = "image_Src")
    String imageSrc;
}

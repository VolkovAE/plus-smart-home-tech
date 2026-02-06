package ru.practicum.smart.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.ProductState;
import ru.practicum.smart.enums.product.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    UUID productId;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    String productName;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    String description;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    QuantityState quantityState;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    ProductState productState;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    ProductCategory productCategory;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    BigDecimal price;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    String imageSrc;
}

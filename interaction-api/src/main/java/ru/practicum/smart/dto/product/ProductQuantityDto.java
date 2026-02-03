package ru.practicum.smart.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import ru.practicum.smart.enums.product.QuantityState;

@Data
@EqualsAndHashCode(of = {"productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductQuantityDto {
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_WRITE, value = "productId")
    String productId;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_WRITE, value = "quantityState")
    QuantityState quantityState;
}

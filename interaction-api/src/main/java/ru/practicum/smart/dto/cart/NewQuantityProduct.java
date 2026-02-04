package ru.practicum.smart.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(of = {"productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewQuantityProduct {
    @NotNull
    private String productId;

    @NotNull
    @Min(value = 1, message = "Количество не может быть меньше 1.")
    private Integer newQuantity;
}

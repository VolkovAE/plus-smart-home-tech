package ru.practicum.smart.dto.warehouse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(of = {"productId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductDto {
    @NotNull
    String productId;

    DimensionDto dimension;

    @NotNull
    Double weight;

    @NotNull
    Boolean fragile;
}

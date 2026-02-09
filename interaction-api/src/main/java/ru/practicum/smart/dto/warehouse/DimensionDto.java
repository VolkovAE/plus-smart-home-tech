package ru.practicum.smart.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @NotNull
    Double depth;

    @NotNull
    Double height;

    @NotNull
    Double width;
}

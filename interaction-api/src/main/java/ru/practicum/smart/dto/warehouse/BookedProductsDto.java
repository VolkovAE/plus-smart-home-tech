package ru.practicum.smart.dto.warehouse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"deliveryWeight", "deliveryVolume", "fragile"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
}

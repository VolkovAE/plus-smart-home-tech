package ru.practicum.smart.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.smart.dto.warehouse.NewProductDto;
import ru.practicum.smart.model.QuantityProductInWarehouse;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ProductMapper {
    public QuantityProductInWarehouse toProduct(NewProductDto dto) {
        return QuantityProductInWarehouse.builder()
                .productId(dto.getProductId())
                .depth(toBigDecimal(dto.getDimension().getDepth()))
                .height(toBigDecimal(dto.getDimension().getHeight()))
                .width(toBigDecimal(dto.getDimension().getWidth()))
                .quantity(0)
                .weight(toBigDecimal(dto.getWeight()))
                .fragile(dto.getFragile())
                .build();
    }

    private BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }
}

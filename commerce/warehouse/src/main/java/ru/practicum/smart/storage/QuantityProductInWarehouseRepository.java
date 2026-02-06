package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.QuantityProductInWarehouse;

import java.util.UUID;

public interface QuantityProductInWarehouseRepository extends JpaRepository<QuantityProductInWarehouse, UUID> {
}

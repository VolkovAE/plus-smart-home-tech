package ru.practicum.smart.storage;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.practicum.smart.model.QuantityProductInWarehouse;

import java.util.Optional;
import java.util.UUID;

public interface QuantityProductInWarehouseRepository extends JpaRepository<QuantityProductInWarehouse, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<QuantityProductInWarehouse> findWithLockByProductId(UUID id);
}

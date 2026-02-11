package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}

package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}

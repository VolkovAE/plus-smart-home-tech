package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.CartProducts;
import ru.practicum.smart.model.CartProductsId;

public interface CartProductsRepository extends JpaRepository<CartProducts, CartProductsId> {
}

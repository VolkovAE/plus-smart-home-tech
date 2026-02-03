package ru.practicum.smart.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable pageable);
}

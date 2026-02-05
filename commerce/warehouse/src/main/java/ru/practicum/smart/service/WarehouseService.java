package ru.practicum.smart.service;

import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.warehouse.AddProductOnWarehouse;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.dto.warehouse.BookedProductsDto;
import ru.practicum.smart.dto.warehouse.NewProductDto;

public interface WarehouseService {
    void addNewProduct(NewProductDto newProductDto);

    void addProductOnWarehouse(AddProductOnWarehouse addProductOnWarehouse);

    AddressDto getAddress();

    BookedProductsDto checkProductOnWarehouse(CartDto cartDto);
}

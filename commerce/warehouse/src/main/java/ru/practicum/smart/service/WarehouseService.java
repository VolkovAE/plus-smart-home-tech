package ru.practicum.smart.service;

import ru.practicum.smart.dto.delivery.ShippedOrderDelivery;
import ru.practicum.smart.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProduct(NewProductDto newProductDto);

    void addProductOnWarehouse(AddProductOnWarehouse addProductOnWarehouse);

    AddressDto getAddress();

    BookedProductsDto checkProductOnWarehouse(Map<UUID, Integer> products);

    void ShippedOrderToDelivery(ShippedOrderDelivery shippedOrderDelivery);

    void returnProducts(Map<UUID, Integer> products);

    BookedProductsDto AssemblyToDelivery(AssemblyProductsForOrderRequest request);
}

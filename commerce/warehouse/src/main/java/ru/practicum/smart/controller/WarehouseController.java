package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.delivery.ShippedOrderDelivery;
import ru.practicum.smart.dto.feign.WarehouseClient;
import ru.practicum.smart.dto.warehouse.*;
import ru.practicum.smart.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@RestController
@RequestMapping(path = PATH_WAREHOUSE)
@Validated
@Slf4j
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Override
    @PutMapping
    public void addNewProduct(@Valid @RequestBody NewProductDto newProductDto) {
        warehouseService.addNewProduct(newProductDto);
    }

    @Override
    @PostMapping(PATH_WAREHOUSE_ADD)
    public void addProductOnWarehouse(@Valid @RequestBody AddProductOnWarehouse addProductOnWarehouse) {
        warehouseService.addProductOnWarehouse(addProductOnWarehouse);
    }

    @Override
    @GetMapping(PATH_WAREHOUSE_ADDRESS)
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @Override
    @PostMapping(PATH_WAREHOUSE_CHECK)
    public BookedProductsDto checkProductOnWarehouse(@Valid @RequestBody CartDto cartDto) {
        return warehouseService.checkProductOnWarehouse(cartDto.getProducts());
    }

    @Override
    @PostMapping("/shipped")
    public void ShippedOrderToDelivery(@Valid @RequestBody ShippedOrderDelivery shippedOrderDelivery) {
        warehouseService.ShippedOrderToDelivery(shippedOrderDelivery);
    }

    @Override
    @PostMapping("/return")
    public void returnProducts(@Valid @RequestBody Map<UUID, Integer> products) {
        warehouseService.returnProducts(products);
    }

    @Override
    @PostMapping("/assembly")
    public BookedProductsDto AssemblyToDelivery(@Valid @RequestBody AssemblyProductsForOrderRequest request) {
        return warehouseService.AssemblyToDelivery(request);
    }
}

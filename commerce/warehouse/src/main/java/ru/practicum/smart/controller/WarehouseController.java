package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.warehouse.AddProductOnWarehouse;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.dto.warehouse.BookedProductsDto;
import ru.practicum.smart.dto.warehouse.NewProductDto;
import ru.practicum.smart.service.WarehouseService;

@RestController
@RequestMapping(path = "/api/v1/warehouse")
@Validated
@Slf4j
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PutMapping
    public void addNewProduct(@Valid @RequestBody NewProductDto newProductDto) {
        warehouseService.addNewProduct(newProductDto);
    }

    @PostMapping("/add")
    public void addProductOnWarehouse(@Valid @RequestBody AddProductOnWarehouse addProductOnWarehouse) {
        warehouseService.addProductOnWarehouse(addProductOnWarehouse);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductOnWarehouse(@Valid @RequestBody CartDto cartDto) {
        return warehouseService.checkProductOnWarehouse(cartDto);
    }
}

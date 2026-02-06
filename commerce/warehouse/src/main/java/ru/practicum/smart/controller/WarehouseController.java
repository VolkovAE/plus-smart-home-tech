package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.feign.WarehouseClient;
import ru.practicum.smart.dto.warehouse.AddProductOnWarehouse;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.dto.warehouse.BookedProductsDto;
import ru.practicum.smart.dto.warehouse.NewProductDto;
import ru.practicum.smart.service.WarehouseService;

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
        return warehouseService.checkProductOnWarehouse(cartDto);
    }
}

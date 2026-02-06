package ru.practicum.smart.dto.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.warehouse.AddProductOnWarehouse;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.dto.warehouse.BookedProductsDto;
import ru.practicum.smart.dto.warehouse.NewProductDto;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_WAREHOUSE, path = PATH_WAREHOUSE)
public interface WarehouseClient {
    @PutMapping
    void addNewProduct(@Valid @RequestBody NewProductDto newProductDto);

    @PostMapping(PATH_WAREHOUSE_ADD)
    void addProductOnWarehouse(@Valid @RequestBody AddProductOnWarehouse addProductOnWarehouse);

    @GetMapping(PATH_WAREHOUSE_ADDRESS)
    AddressDto getAddress();

    @PostMapping(PATH_WAREHOUSE_CHECK)
    BookedProductsDto checkProductOnWarehouse(@Valid @RequestBody CartDto cartDto);
}

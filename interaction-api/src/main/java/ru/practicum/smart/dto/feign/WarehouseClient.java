package ru.practicum.smart.dto.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.delivery.ShippedOrderDelivery;
import ru.practicum.smart.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/shipped")
    void ShippedOrderToDelivery(@Valid @RequestBody ShippedOrderDelivery shipOrder);

    @PostMapping("/return")
    void returnProducts(@Valid @RequestBody Map<UUID, Integer> products);

    @PostMapping("/assembly")
    BookedProductsDto AssemblyToDelivery(@Valid @RequestBody AssemblyProductsForOrderRequest request);
}

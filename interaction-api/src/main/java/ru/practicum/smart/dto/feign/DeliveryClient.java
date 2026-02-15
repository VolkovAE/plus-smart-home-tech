package ru.practicum.smart.dto.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.smart.dto.delivery.DeliveryDto;
import ru.practicum.smart.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_DELIVERY, path = PATH_DELIVERY)
public interface DeliveryClient {
    @PutMapping
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto);

    @PostMapping(PATH_DELIVERY_SUCCESSFUL)
    void deliverySuccessful(@PathVariable UUID orderId);

    @PostMapping(PATH_DELIVERY_PICKED)
    void deliveryPicked(@PathVariable UUID orderId);

    @PostMapping(PATH_DELIVERY_FAILED)
    void deliveryFailed(@PathVariable UUID orderId);

    @PostMapping(PATH_DELIVERY_COST)
    BigDecimal getDeliveryCost(@RequestBody @Valid OrderDto orderDto);
}

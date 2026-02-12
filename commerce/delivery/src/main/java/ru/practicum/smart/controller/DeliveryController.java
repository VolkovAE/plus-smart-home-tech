package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.delivery.DeliveryDto;
import ru.practicum.smart.dto.feign.DeliveryClient;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@RestController
@RequestMapping(path = PATH_DELIVERY)
@Slf4j
@Validated
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    @PutMapping
    public DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    @PostMapping(PATH_DELIVERY_SUCCESSFUL)
    public void deliverySuccessful(@RequestBody UUID orderId) {
        deliveryService.deliverySuccessful(orderId);
    }

    @Override
    @PostMapping(PATH_DELIVERY_PICKED)
    public void deliveryPicked(@RequestBody UUID orderId) {
        deliveryService.deliveryPicked(orderId);
    }

    @Override
    @PostMapping(PATH_DELIVERY_FAILED)
    public void deliveryFailed(@RequestBody UUID orderId) {
        deliveryService.deliveryFailed(orderId);
    }

    @Override
    @PostMapping(PATH_DELIVERY_COST)
    public BigDecimal getDeliveryCost(@RequestBody @Valid OrderDto orderDto) {
        return deliveryService.getDeliveryCost(orderDto);
    }
}

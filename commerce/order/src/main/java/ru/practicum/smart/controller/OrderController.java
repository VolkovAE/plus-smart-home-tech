package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.feign.OrderClient;
import ru.practicum.smart.dto.order.CreateNewOrderRequest;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.order.ProductReturnRequest;
import ru.practicum.smart.service.OrderService;

import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@Slf4j
@RestController
@RequestMapping(path = PATH_SHOPPING_ORDER)
@Validated
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @GetMapping
    public Page<OrderDto> getOrdersByUser(@RequestParam(REQUESTPARAM_ORDER_USERNAME) @NotBlank String username,
                                          Pageable pageable) {
        return orderService.getOrdersByUser(username, pageable);
    }

    @Override
    @PostMapping
    public OrderDto createOrder(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest) {
        return orderService.createOrder(createNewOrderRequest);
    }

    @Override
    @PostMapping(PATH_ORDER_RETURN)
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest) {
        return orderService.returnOrder(productReturnRequest);
    }

    @Override
    @PostMapping(PATH_ORDER_PAYMENT)
    public OrderDto paymentComplete(@RequestBody UUID orderId) {
        return orderService.paymentComplete(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_PAYMENT_FAILED)
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_DELIVERY)
    public OrderDto orderDelivered(@RequestBody UUID orderId) {
        return orderService.orderDelivered(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_DELIVERY_FAILED)
    public OrderDto orderDeliveryFailed(@RequestBody UUID orderId) {
        return orderService.orderDeliveryFailed(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_COMPLECTED)
    public OrderDto orderCompleted(@RequestBody UUID orderId) {
        return orderService.orderCompleted(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_CALCULATE_TOTAL)
    public OrderDto orderCalculateTotal(@RequestBody UUID orderId) {
        return orderService.orderCalculateTotal(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_CALCULATE_DELIVERY)
    public OrderDto orderCalculateDelivery(@RequestBody UUID orderId) {
        return orderService.orderCalculateDelivery(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_ASSEMBLY)
    public OrderDto orderAssembled(@RequestBody UUID orderId) {
        return orderService.orderAssembled(orderId);
    }

    @Override
    @PostMapping(PATH_ORDER_ASSEMBLY_FAILED)
    public OrderDto orderAssembleFailed(@RequestBody UUID orderId) {
        return orderService.orderAssembleFailed(orderId);
    }
}

package ru.practicum.smart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.smart.dto.order.CreateNewOrderRequest;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {
    Page<OrderDto> getOrdersByUser(String username, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto paymentComplete(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto orderDelivered(UUID orderId);

    OrderDto orderDeliveryFailed(UUID orderId);

    OrderDto orderCompleted(UUID orderId);

    OrderDto orderCalculateTotal(UUID orderId);

    OrderDto orderCalculateDelivery(UUID orderId);

    OrderDto orderAssembled(UUID orderId);

    OrderDto orderAssembleFailed(UUID orderId);
}

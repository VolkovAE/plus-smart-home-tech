package ru.practicum.smart.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.model.Order;
import ru.practicum.smart.model.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(productsToMap(order.getProducts()))
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .orderState(order.getOrderState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    public Map<UUID, Integer> productsToMap(List<OrderItem> products) {
        return products.stream()
                .collect(Collectors.toMap(
                        OrderItem::getProductId,
                        OrderItem::getQuantity
                ));
    }

    public List<OrderItem> productsToOrderItem(Map<UUID, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(entry.getKey());
                    item.setQuantity(entry.getValue());
                    return item;
                })
                .toList();
    }
}

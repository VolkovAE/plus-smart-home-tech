package ru.practicum.smart.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.feign.ShoppingCartClient;
import ru.practicum.smart.dto.feign.WarehouseClient;
import ru.practicum.smart.dto.order.CreateNewOrderRequest;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.order.ProductReturnRequest;
import ru.practicum.smart.enums.order.OrderState;
import ru.practicum.smart.exception.InternalServerErrorException;
import ru.practicum.smart.exception.NoOrderFoundException;
import ru.practicum.smart.exception.NotFoundServiceException;
import ru.practicum.smart.exception.ServiceTemporarilyUnavailableException;
import ru.practicum.smart.mapper.OrderMapper;
import ru.practicum.smart.model.Order;
import ru.practicum.smart.model.OrderItem;
import ru.practicum.smart.storage.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Qualifier("OrderServiceImpl")
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final WarehouseClient warehouseClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderMapper orderMapper,
                            WarehouseClient warehouseClient,
                            ShoppingCartClient shoppingCartClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.warehouseClient = warehouseClient;
        this.shoppingCartClient = shoppingCartClient;
    }

    @Override
    public Page<OrderDto> getOrdersByUser(String username, Pageable pageable) {
        CartDto cartDto = shoppingCartClient.getActiveCart(username);
        Page<Order> orders = orderRepository.getAllByShoppingCartId(cartDto.getCartId(), pageable);
        return orders.map(orderMapper::toOrderDto);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest) {
        CartDto cartDto = createNewOrderRequest.getShoppingCart();

        String extWarehouseException = "Ошибка при проверке количества продукта на складе. Причина: ";

        // Сервис склада - проверка нужного количества продукта на складе.
        try {
            warehouseClient.checkProductOnWarehouse(cartDto);
        } catch (NotFoundServiceException | InternalServerErrorException | FeignException |
                 ServiceTemporarilyUnavailableException e) {
            throwNewException(e, extWarehouseException);
        }

        Order order = new Order();
        order.setShoppingCartId(cartDto.getCartId());
        order.setOrderState(OrderState.NEW);

        List<OrderItem> orderItems = orderMapper.productsToOrderItem(cartDto.getProducts(), order);

        Order newOrder = orderRepository.save(order);

        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        Order order = getOrder(productReturnRequest.getOrderId());

        order.setOrderState(OrderState.PRODUCT_RETURNED);

        Map<UUID, Integer> products = productReturnRequest.getProducts();

        String extWarehouseException = "Ошибка при возврате количества продукта на складе. Причина: ";

        // Сервис склада - проверка нужного количества продукта на складе.
        try {
            warehouseClient.returnProducts(products);
        } catch (NotFoundServiceException | InternalServerErrorException | FeignException |
                 ServiceTemporarilyUnavailableException e) {
            throwNewException(e, extWarehouseException);
        }

        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto paymentComplete(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.PAID);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.PAYMENT_FAILED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderDelivered(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.DELIVERED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderDeliveryFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.DELIVERY_FAILED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderCompleted(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.COMPLETED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderCalculateTotal(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.ON_PAYMENT);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderCalculateDelivery(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.ON_DELIVERY);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderAssembled(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.ASSEMBLED);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto orderAssembleFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setOrderState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(order);
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ c ID " + orderId + " не найден."));
    }

    private <T extends Exception> void throwNewException(T e, String ext) {
        throw new InternalServerErrorException(ext + e.getMessage());
    }
}

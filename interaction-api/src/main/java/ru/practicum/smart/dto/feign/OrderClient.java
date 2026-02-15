package ru.practicum.smart.dto.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.smart.dto.order.CreateNewOrderRequest;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.order.ProductReturnRequest;

import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_ORDER, path = PATH_SHOPPING_ORDER)
public interface OrderClient {
    @GetMapping
    Page<OrderDto> getOrdersByUser(@RequestParam(REQUESTPARAM_ORDER_USERNAME) String username,
                                   @SpringQueryMap Pageable pageable);

    @PutMapping
    OrderDto createOrder(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest);

    @PostMapping(PATH_ORDER_RETURN)
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping(PATH_ORDER_PAYMENT)
    OrderDto paymentComplete(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_PAYMENT_FAILED)
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_DELIVERY)
    OrderDto orderDelivered(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_DELIVERY_FAILED)
    OrderDto orderDeliveryFailed(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_COMPLECTED)
    OrderDto orderCompleted(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_CALCULATE_TOTAL)
    OrderDto orderCalculateTotal(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_CALCULATE_DELIVERY)
    OrderDto orderCalculateDelivery(@RequestBody UUID orderId);

    @PostMapping(PATH_ORDER_ASSEMBLY)
    OrderDto orderAssembled(@RequestBody UUID orderId) throws FeignException;

    @PostMapping(PATH_ORDER_ASSEMBLY_FAILED)
    OrderDto orderAssembleFailed(@RequestBody UUID orderId);
}

package ru.practicum.smart.dto.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@FeignClient(name = NAME_SERVICE_PAYMENT, path = PATH_PAYMENT)
public interface PaymentClient {
    @PostMapping
    PaymentDto makePayment(@Valid @RequestBody OrderDto orderDto);

    @PostMapping(PATH_PAYMENT_TOTAL_COST)
    BigDecimal getTotalCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping(PATH_PAYMENT_SUCCESS)
    void paymentSuccess(@RequestBody UUID paymentId);

    @PostMapping(PATH_PAYMENT_PRODUCT_COST)
    BigDecimal getProductCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping(PATH_PAYMENT_FAILED)
    void paymentFailed(@RequestBody UUID paymentId);
}

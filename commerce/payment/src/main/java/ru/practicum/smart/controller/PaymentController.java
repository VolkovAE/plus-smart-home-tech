package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.smart.dto.feign.PaymentClient;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.payment.PaymentDto;
import ru.practicum.smart.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.practicum.smart.dto.util.StringConstants.*;

@RestController
@RequestMapping(path = PATH_PAYMENT)
@Slf4j
@Validated
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(@Qualifier("PaymentServiceImpl") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @PostMapping
    public PaymentDto makePayment(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.makePayment(orderDto);
    }

    @Override
    @PostMapping(PATH_PAYMENT_TOTAL_COST)
    public BigDecimal getTotalCost(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    @PostMapping(PATH_PAYMENT_SUCCESS)
    public void paymentSuccess(@RequestBody UUID paymentId) {
        paymentService.paymentSuccess(paymentId);
    }

    @Override
    @PostMapping(PATH_PAYMENT_PRODUCT_COST)
    public BigDecimal getProductCost(@RequestBody @Valid OrderDto orderDto) {
        return paymentService.getProductCost(orderDto);
    }

    @Override
    @PostMapping(PATH_PAYMENT_FAILED)
    public void paymentFailed(@RequestBody UUID paymentId) {
        paymentService.paymentFailed(paymentId);
    }
}

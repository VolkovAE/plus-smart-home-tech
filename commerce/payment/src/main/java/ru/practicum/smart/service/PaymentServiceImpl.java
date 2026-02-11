package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.feign.OrderClient;
import ru.practicum.smart.dto.feign.ShoppingStoreClient;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.payment.PaymentDto;
import ru.practicum.smart.enums.payment.PaymentState;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.mapper.PaymentMapper;
import ru.practicum.smart.model.Payment;
import ru.practicum.smart.storage.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Qualifier("PaymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    private final BigDecimal nds = BigDecimal.valueOf(0.1); // НДС 10%

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              OrderClient orderClient,
                              ShoppingStoreClient shoppingStoreClient) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.orderClient = orderClient;
        this.shoppingStoreClient = shoppingStoreClient;
    }

    @Override
    public PaymentDto makePayment(OrderDto orderDto) {
        Payment payment = new Payment();
        payment.setOrderId(orderDto.getOrderId());
        payment.setState(PaymentState.PENDING);
        payment.setTotalPayment(getTotalCost(orderDto));
        payment.setDeliveryTotal(getDeliveryCost(orderDto));
        payment.setFeeTotal(getProductCost(orderDto).multiply(nds));

        Payment newPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(newPayment);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        BigDecimal productCost = getProductCost(orderDto);

        BigDecimal deliveryCost = getDeliveryCost(orderDto);

        BigDecimal feeCost = productCost.multiply(nds);

        return productCost.add(deliveryCost).add(feeCost);
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.SUCCESS);

        orderClient.paymentComplete(payment.getOrderId());
    }

    @Override
    public BigDecimal getProductCost(OrderDto orderDto) {
        BigDecimal productCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> entry : orderDto.getProducts().entrySet()) {
            BigDecimal cost = shoppingStoreClient.getProduct(entry.getKey()).getPrice();

            productCost = productCost.add(cost.multiply(BigDecimal.valueOf(entry.getValue())));
        }

        return productCost;
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.FAILED);

        orderClient.paymentFailed(payment.getOrderId());
    }

    private BigDecimal getDeliveryCost(OrderDto orderDto) {
        BigDecimal deliveryCost = orderDto.getDeliveryPrice();

        if (deliveryCost == null || deliveryCost.equals(BigDecimal.ZERO)) {
            //deliveryCost = deliveryClient.getDeliveryCost(orderDto);    // todo переделать определение стоимости доставки когда готов delivery
        }

        return deliveryCost;
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() ->
                new NotFoundException("Payment с ID " + paymentId + " не найден."));
    }
}

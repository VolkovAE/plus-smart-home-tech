package ru.practicum.smart.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.delivery.DeliveryDto;
import ru.practicum.smart.dto.delivery.ShippedOrderDelivery;
import ru.practicum.smart.dto.feign.OrderClient;
import ru.practicum.smart.dto.feign.WarehouseClient;
import ru.practicum.smart.dto.order.OrderDto;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.enums.delivery.DeliveryState;
import ru.practicum.smart.exception.InternalServerErrorException;
import ru.practicum.smart.exception.NoDeliveryFoundException;
import ru.practicum.smart.exception.NotFoundServiceException;
import ru.practicum.smart.exception.ServiceTemporarilyUnavailableException;
import ru.practicum.smart.mapper.DeliveryMapper;
import ru.practicum.smart.model.Delivery;
import ru.practicum.smart.storage.DeliveryRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private final BigDecimal baseCost = BigDecimal.valueOf(5);
    private final BigDecimal address1 = BigDecimal.valueOf(1);
    private final BigDecimal address2 = BigDecimal.valueOf(2);
    private final BigDecimal fragile = BigDecimal.valueOf(0.2);
    private final BigDecimal weight = BigDecimal.valueOf(0.3);
    private final BigDecimal volume = BigDecimal.valueOf(0.2);
    private final BigDecimal deliveryAddress = BigDecimal.valueOf(0.2);

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository,
                               DeliveryMapper deliveryMapper,
                               OrderClient orderClient,
                               WarehouseClient warehouseClient) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
        this.orderClient = orderClient;
        this.warehouseClient = warehouseClient;
    }

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);

        Delivery newDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDeliveryDto(newDelivery);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);

        delivery.setDeliveryState(DeliveryState.DELIVERED);
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        String extOrderException = "Ошибка при установке заказу с ID " + orderId + " статуса на собран. Причина: ";
        String extWarehouseException = "Ошибка при указании заказа в бронировании. Причина: ";

        // Сервис заказа - статус собран.
        try {
            orderClient.orderAssembled(orderId);
        } catch (NotFoundServiceException | InternalServerErrorException | FeignException |
                 ServiceTemporarilyUnavailableException e) {
            throwNewException(e, extOrderException);
        }

        Delivery delivery = getDeliveryByOrderId(orderId);

        ShippedOrderDelivery shippedOrderDelivery = new ShippedOrderDelivery(orderId, delivery.getDeliveryId());

        // Сервис склада - отправка в доставку (укажем в бронировании id доставки).
        try {
            warehouseClient.ShippedOrderToDelivery(shippedOrderDelivery);
        } catch (NotFoundServiceException | InternalServerErrorException | FeignException |
                 ServiceTemporarilyUnavailableException e) {
            throwNewException(e, extWarehouseException);
        }

        // Если все сервисы отработали успешно, меняем статус доставки.
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
    }

    @Override
    public BigDecimal getDeliveryCost(OrderDto orderDto) {
        Delivery delivery = getDeliveryByOrderId(orderDto.getOrderId());

        BigDecimal totalCost = baseCost;

        BigDecimal warehouseAddrCoff = BigDecimal.ZERO;
        if (isWarehouseAddress("ADDRESS_1")) warehouseAddrCoff = address1;
        else if (isWarehouseAddress("ADDRESS_2")) warehouseAddrCoff = address2;
        totalCost = totalCost.add(totalCost.multiply(warehouseAddrCoff));

        if (orderDto.getFragile()) totalCost = totalCost.add(totalCost.multiply(fragile));

        totalCost = totalCost.add(weight.multiply(BigDecimal.valueOf(orderDto.getDeliveryWeight())));
        totalCost = totalCost.add(volume.multiply(BigDecimal.valueOf(orderDto.getDeliveryVolume())));

        if (!delivery.getFromAddress().getStreet().equals(warehouseClient.getAddress().getStreet()))
            totalCost = totalCost.add(deliveryAddress.multiply(totalCost));

        return totalCost;
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(() ->
                new NoDeliveryFoundException("Доставка заказа с ID " + orderId + " не найдена."));
    }

    private boolean isWarehouseAddress(String adr) {
        AddressDto address = warehouseClient.getAddress();

        return (address.getCountry().contains(adr)
                || address.getCity().contains(adr)
                || address.getStreet().contains(adr)
                || address.getHouse().contains(adr)
                || address.getFlat().contains(adr)
        );
    }

    private <T extends Exception> void throwNewException(T e, String ext) {
        throw new InternalServerErrorException(ext + e.getMessage());
    }
}

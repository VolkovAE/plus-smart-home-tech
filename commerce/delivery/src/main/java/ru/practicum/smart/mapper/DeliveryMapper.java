package ru.practicum.smart.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.smart.dto.delivery.DeliveryDto;
import ru.practicum.smart.model.Delivery;

@Component
public class DeliveryMapper {
    private final AddressMapper addressMapper;

    @Autowired
    public DeliveryMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Delivery toDelivery(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .deliveryId(deliveryDto.getDeliveryId())
                .deliveryState(deliveryDto.getDeliveryState())
                .orderId(deliveryDto.getOrderId())
                .fromAddress(addressMapper.toAddress(deliveryDto.getFromAddress()))
                .toAddress(addressMapper.toAddress(deliveryDto.getToAddress()))
                .build();
    }

    public DeliveryDto toDeliveryDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .deliveryState(delivery.getDeliveryState())
                .orderId(delivery.getOrderId())
                .fromAddress(addressMapper.toAddressDto(delivery.getFromAddress()))
                .toAddress(addressMapper.toAddressDto(delivery.getToAddress()))
                .build();
    }
}

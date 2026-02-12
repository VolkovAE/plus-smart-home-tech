package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.delivery.ShippedOrderDelivery;
import ru.practicum.smart.dto.warehouse.*;
import ru.practicum.smart.exception.DuplicatedDataException;
import ru.practicum.smart.exception.NoOrderFoundException;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.NotRequestQuantityProductException;
import ru.practicum.smart.mapper.ProductMapper;
import ru.practicum.smart.model.OrderBooking;
import ru.practicum.smart.model.QuantityProductInWarehouse;
import ru.practicum.smart.storage.OrderBookingRepository;
import ru.practicum.smart.storage.QuantityProductInWarehouseRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final QuantityProductInWarehouseRepository warehouseRepository;
    private final ProductMapper productMapper;
    private final OrderBookingRepository orderBookingRepository;

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Autowired
    public WarehouseServiceImpl(QuantityProductInWarehouseRepository warehouseRepository,
                                ProductMapper productMapper,
                                OrderBookingRepository orderBookingRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productMapper = productMapper;
        this.orderBookingRepository = orderBookingRepository;
    }

    public void addNewProduct(NewProductDto newProductDto) {
        if (warehouseRepository.existsById(newProductDto.getProductId())) {
            throw new DuplicatedDataException("Продукт с ID = " + newProductDto.getProductId() + " уже зарегистрирован на складе.");
        }

        QuantityProductInWarehouse product = productMapper.toProduct(newProductDto);

        warehouseRepository.save(product);
    }

    public void addProductOnWarehouse(AddProductOnWarehouse addProductOnWarehouse) {
        QuantityProductInWarehouse product = getProduct(addProductOnWarehouse.getProductId());

        product.setQuantity(product.getQuantity() + addProductOnWarehouse.getQuantity());

        warehouseRepository.save(product);
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    public BookedProductsDto checkProductOnWarehouse(Map<UUID, Integer> products) {
        BigDecimal deliveryWeight = BigDecimal.ZERO;
        BigDecimal deliveryVolume = BigDecimal.ZERO;
        boolean fragile = false;   // если один продукт хрупкий, то считаем true

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Integer requestQuantity = entry.getValue();

            QuantityProductInWarehouse product = getProduct(productId);
            if (product.getQuantity() < requestQuantity)
                throw new NotRequestQuantityProductException("Нет требуемого количества товара ID = " + productId + ", остаток " + product.getQuantity() + ", требуется " + requestQuantity + ".");

            BigDecimal bigDecimalRequestQuantity = new BigDecimal(requestQuantity);

            // Накапливаем вес по продуктам от их количества
            deliveryWeight = deliveryWeight.add(product.getWeight().multiply(bigDecimalRequestQuantity));

            // Накапливаем объем по продуктам от их количества
            BigDecimal volume = product.getDepth()
                    .multiply(product.getHeight())
                    .multiply(product.getWeight())
                    .multiply(bigDecimalRequestQuantity);

            deliveryVolume = deliveryVolume.add(volume);

            // хрупкий
            if (product.getFragile()) fragile = true;
        }

        return new BookedProductsDto(deliveryWeight.doubleValue(), deliveryVolume.doubleValue(), fragile);
    }

    private QuantityProductInWarehouse getProduct(UUID productId) {
        return warehouseRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("На складе нет продукта с ID " + productId + "."));
    }

    @Override
    public void ShippedOrderToDelivery(ShippedOrderDelivery shippedOrderDelivery) {
        OrderBooking orderBooking = orderBookingRepository.findByOrderId(shippedOrderDelivery.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("Бронирование заказа с ID " + shippedOrderDelivery.getOrderId() + " не найдена."));

        orderBooking.setDeliveryId(shippedOrderDelivery.getDeliveryId());

        orderBookingRepository.save(orderBooking);
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            QuantityProductInWarehouse product = warehouseRepository.findWithLockByProductId(entry.getKey())
                    .orElseThrow(() -> new NotFoundException("Продукт с ID " + entry.getKey() + " не найден."));

            product.setQuantity(product.getQuantity() + entry.getValue());
        }
    }

    @Override
    public BookedProductsDto AssemblyToDelivery(AssemblyProductsForOrderRequest request) {
        UUID orderId = request.getOrderId();
        Map<UUID, Integer> products = request.getProducts();

        BookedProductsDto bookedProductsDto = checkProductOnWarehouse(products);

        OrderBooking orderBooking = orderBookingRepository.findByOrderId(orderId)
                .orElseGet(() -> OrderBooking.builder().orderId(orderId).build());

        orderBooking.setDeliveryWeight(BigDecimal.valueOf(bookedProductsDto.getDeliveryWeight()));
        orderBooking.setDeliveryVolume(BigDecimal.valueOf(bookedProductsDto.getDeliveryVolume()));
        orderBooking.setFragile(bookedProductsDto.getFragile());
        orderBooking.getBookingProducts().clear();

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Integer requestedQuantity = entry.getValue();

            QuantityProductInWarehouse product = warehouseRepository.findWithLockByProductId(productId)
                    .orElseThrow(() -> new NotFoundException("Продукт с ID " + productId + " не существует"));
            product.setQuantity(product.getQuantity() - requestedQuantity);

            orderBooking.addProduct(product, requestedQuantity);
        }

        orderBookingRepository.save(orderBooking);

        return bookedProductsDto;
    }
}

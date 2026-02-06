package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.warehouse.AddProductOnWarehouse;
import ru.practicum.smart.dto.warehouse.AddressDto;
import ru.practicum.smart.dto.warehouse.BookedProductsDto;
import ru.practicum.smart.dto.warehouse.NewProductDto;
import ru.practicum.smart.exception.DuplicatedDataException;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.NotRequestQuantityProductException;
import ru.practicum.smart.mapper.ProductMapper;
import ru.practicum.smart.model.QuantityProductInWarehouse;
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

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Autowired
    public WarehouseServiceImpl(QuantityProductInWarehouseRepository warehouseRepository, ProductMapper productMapper) {
        this.warehouseRepository = warehouseRepository;
        this.productMapper = productMapper;
    }

    public void addNewProduct(NewProductDto newProductDto) {
        if (warehouseRepository.existsById(UUID.fromString(newProductDto.getProductId()))) {
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
    public BookedProductsDto checkProductOnWarehouse(CartDto cartDto) {
        BigDecimal deliveryWeight = BigDecimal.ZERO;
        BigDecimal deliveryVolume = BigDecimal.ZERO;
        boolean fragile = false;   // если один продукт хрупкий, то считаем true

        for (Map.Entry<String, Integer> entry : cartDto.getProducts().entrySet()) {
            String productId = entry.getKey();
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

    private QuantityProductInWarehouse getProduct(String productId) {
        return warehouseRepository.findById(UUID.fromString(productId)).orElseThrow(() ->
                new NotFoundException("На складе нет продукта с ID " + productId + "."));
    }
}

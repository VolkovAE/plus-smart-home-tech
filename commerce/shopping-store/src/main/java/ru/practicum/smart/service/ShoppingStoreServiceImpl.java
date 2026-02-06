package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.ProductState;
import ru.practicum.smart.enums.product.QuantityState;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.mapper.ProductMapper;
import ru.practicum.smart.model.Product;
import ru.practicum.smart.storage.ProductRepository;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("StoreServiceImpl")
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ShoppingStoreServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        Page<Product> products = productRepository.findAllByProductCategory(category, pageable);

        Page<ProductDto> productDtos = products.map(productMapper::toProductDto);

        return productDtos;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        Product product = getProductById(productId);

        return productMapper.toProductDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        UUID productId = productDto.getProductId();

        if (productId != null) {
            // в описании к заданию передается, в тестах гита убрали:(
            if (checkIfProductExists(productId))
                throw new ValidationException("Продукт с ID " + productId + " уже существует.", log);
        }

        Product product = productMapper.toProduct(productDto);
        Product newProduct = productRepository.save(product);

        log.info("Создан новый продукт с ID {}", newProduct.getProductId());

        return productMapper.toProductDto(newProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        UUID productId = productDto.getProductId();

        if (!checkIfProductExists(productId))
            throw new NotFoundException("Продукт с ID " + productId + " не существует.", log);

        Product product = productMapper.toProduct(productDto);
        Product newProduct = productRepository.save(product);

        return productMapper.toProductDto(newProduct);
    }

    @Override
    public Boolean deleteProduct(UUID productId) {
        log.info("Ситуация 1. Запрос на удаление продукта с ID = {}", productId);

        Product product = getProductById(productId);

        log.info("Ситуация 1. Продукт найден с ID = {}", product.toString());

        product.setProductState(ProductState.DEACTIVATE);

        log.info("Ситуация 1. Изменено состояние объекта = {}", product.getProductState());

        Product newProduct = productRepository.save(product);

        log.info("Ситуация 1. Записано состояние объекта в БД = {}", newProduct.getProductState());
        log.info("Ситуация 1. Возвращаемый результат = {}", newProduct.getProductState().equals(ProductState.DEACTIVATE));

        return newProduct.getProductState().equals(ProductState.DEACTIVATE);
    }

//    @Override
//    public Boolean updateQuantity(ProductQuantityDto productQuantityDto) {
//        log.info("Ситуация 2. Запрос на изменение количества {}", productQuantityDto);
//
//        Product product = getProductById(productQuantityDto.getProductId());
//
//        log.info("Ситуация 2. Получен продукт {}", product);
//
//        product.setQuantityState(productQuantityDto.getQuantityState());
//
//        log.info("Ситуация 2. Установил объекту новое значение количества {}", product);
//
//        Product newProduct = productRepository.save(product);
//
//        log.info("Ситуация 2. Записал в БД новое значение количества {}", newProduct);
//
//        log.info("Ситуация 2. Ожидаемый возврат {}", newProduct.getQuantityState().equals(productQuantityDto.getQuantityState()));
//
//        return newProduct.getQuantityState().equals(productQuantityDto.getQuantityState());
//    }

    @Override
    public Boolean updateQuantity(UUID productId, QuantityState quantityState) {
        log.info("Ситуация 2.1 Запрос на изменение количества продукта ID {}", productId);

        Product product = getProductById(productId);

        log.info("Ситуация 2.1 Получен продукт {}", product);

        product.setQuantityState(quantityState);

        log.info("Ситуация 2.1 Установил объекту новое значение количества {}", product);

        Product newProduct = productRepository.save(product);

        log.info("Ситуация 2.1 Записал в БД новое значение количества {}", newProduct);

        log.info("Ситуация 2.1 Ожидаемый возврат {}", newProduct.getQuantityState().equals(quantityState));

        return newProduct.getQuantityState().equals(quantityState);
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Продукт с ID " + productId + " не существует", log));
    }

    private boolean checkIfProductExists(UUID productId) {
        return productRepository.existsById(productId);
    }
}

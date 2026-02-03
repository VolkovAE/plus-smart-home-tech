package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.product.ProductDto;
import ru.practicum.smart.dto.product.ProductQuantityDto;
import ru.practicum.smart.enums.product.ProductCategory;
import ru.practicum.smart.enums.product.ProductState;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.mapper.ProductMapper;
import ru.practicum.smart.model.Product;
import ru.practicum.smart.storage.ProductRepository;

@Slf4j
@Service
@Qualifier("SensorServiceImpl")
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
    public ProductDto getProduct(String productId) {
        Product product = getProductById(productId);

        return productMapper.toProductDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = productDto.getProductId();

        if (checkIfProductExists(productId))
            throw new ValidationException("Продукт с ID " + productId + " уже существует.", log);

        Product product = productMapper.toProduct(productDto);
        Product newProduct = productRepository.save(product);

        return productMapper.toProductDto(newProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        String productId = productDto.getProductId();

        if (!checkIfProductExists(productId))
            throw new NotFoundException("Продукт с ID " + productId + " не существует.", log);

        Product product = productMapper.toProduct(productDto);
        Product newProduct = productRepository.save(product);

        return productMapper.toProductDto(newProduct);
    }

    @Override
    public Boolean deleteProduct(String productId) {
        Product product = getProductById(productId);

        product.setProductState(ProductState.DEACTIVATE);

        Product newProduct = productRepository.save(product);

        return newProduct.getProductState().equals(ProductState.DEACTIVATE);
    }

    @Override
    public Boolean updateQuantity(ProductQuantityDto productQuantityDto) {
        Product product = getProductById(productQuantityDto.getProductId());

        product.setQuantityState(productQuantityDto.getQuantityState());
        Product newProduct = productRepository.save(product);

        return newProduct.getQuantityState().equals(productQuantityDto.getQuantityState());
    }

    private Product getProductById(String productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("Продукт с ID " + productId + " не существует", log));
    }

    private boolean checkIfProductExists(String productId) {
        return productRepository.existsById(productId);
    }
}

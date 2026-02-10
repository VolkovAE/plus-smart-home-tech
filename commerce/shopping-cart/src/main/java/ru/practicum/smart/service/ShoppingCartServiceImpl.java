package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;
import ru.practicum.smart.dto.feign.WarehouseClient;
import ru.practicum.smart.exception.EmptyUsernameException;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.NotRequestQuantityProductException;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.mapper.CartMapper;
import ru.practicum.smart.model.Cart;
import ru.practicum.smart.model.CartProducts;
import ru.practicum.smart.storage.CartRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.smart.util.StringConstants.*;

@Slf4j
@Service
@Qualifier("CartServiceImpl")
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final WarehouseClient warehouseClient;

    @Autowired
    public ShoppingCartServiceImpl(CartRepository cartRepository,
                                   CartMapper cartMapper,
                                   WarehouseClient warehouseClient) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.warehouseClient = warehouseClient;
    }

    @Override
    public CartDto addProducts(String username, Map<UUID, Integer> products) {
        // Проверка имени пользователя
        checkUserName(username);

        // Проверка наличия пордуктов добавляемых в корзину
        checkProducts(products);

        // Получаем активную карту пользователя, если нет, то создаем
        Cart cart = getActiveCartByUserName(username, true);

        // Добавляем переданные продукты в корзину
        Map<UUID, CartProducts> existingProducts = cart.getProducts().stream()
                .collect(Collectors.toMap(CartProducts::getProductId, Function.identity()));

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity == null || quantity < 1)
                throw new IllegalArgumentException("Не допустимое количество продукта с ID = " + productId + ", равно " + quantity + ".");

            CartProducts existingProduct = existingProducts.get(productId);
            if (existingProduct != null) existingProduct.setQuantity(quantity);
            else {
                CartProducts newProduct = new CartProducts();
                newProduct.setCart(cart);
                newProduct.setProductId(productId);
                newProduct.setQuantity(quantity);

                cart.getProducts().add(newProduct);
            }
        }

        checkProductOnWarehouse(cart);

        cart = cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartDto removeProducts(String username, List<UUID> productIds) {
        // Проверка имени пользователя
        checkUserName(username);

        // Проверка наличия пордуктов добавляемых в корзину
        checkProducts(productIds);

        // Получаем активную карту пользователя
        Cart cart = getActiveCartByUserName(username, false);

        // Удаляем продукты из корзины
        Set<UUID> curProductIds = cart.getProducts().stream()
                .map(CartProducts::getProductId)
                .collect(Collectors.toSet());

        // Проверка, что продукты для удаления есть в корзине
        if (!curProductIds.containsAll(productIds))
            throw new NotFoundException(MESSAGE_IF_NOT_PRODUCTS_IN_CARD);

        // Удаляем товары из корзины
        cart.getProducts().removeIf(cartProducts -> productIds.contains(cartProducts.getProductId()));

        cart = cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartDto changeQuantity(String username, NewQuantityProduct changeQuantity) {
        // Проверка имени пользователя
        checkUserName(username);

        // Проверяем наличие данных для изменения
        if (changeQuantity == null)
            throw new ValidationException("Нет данных для изменения количества продуктов.");

        // Получаем активную карту пользователя
        Cart cart = getActiveCartByUserName(username, false);

        // Изменяем количество по одному из продуктов
        UUID productId = changeQuantity.getProductId();
        Integer newQuantity = changeQuantity.getNewQuantity();

        Optional<CartProducts> existingProduct = cart.getProducts().stream()
                .filter(cartProducts -> cartProducts.getProductId().equals(productId))
                .findFirst();
        if (existingProduct.isEmpty()) throw new NotFoundException("Продукт не найден в корзине.");

        existingProduct.get().setQuantity(newQuantity);

        checkProductOnWarehouse(cart);

        cart = cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartDto getActiveCart(String username) {
        // Проверка имени пользователя
        checkUserName(username);

        // Получаем активную карту пользователя, если нет, то создаем
        Cart cart = getActiveCartByUserName(username, true);

        return cartMapper.toCartDto(cart);
    }

    @Override
    public Boolean deactivate(String username) {
        // Проверка имени пользователя
        checkUserName(username);

        // Получаем активную карту пользователя
        Cart cart = getActiveCartByUserName(username, false);

        // Деактивируем карту
        cart.setActive(false);

        cartRepository.save(cart);

        return true;
    }

    private void checkUserName(String username) {
        if (username == null || username.isEmpty())
            throw new EmptyUsernameException(MESSAGE_IF_NOT_NAME_USER);
    }

    private void checkProducts(Map<UUID, Integer> products) {
        if (products == null || products.isEmpty())
            throw new ValidationException(MESSAGE_IF_NOT_PRODUCTS);
    }

    private void checkProducts(List<UUID> products) {
        if (products == null || products.isEmpty())
            throw new ValidationException(MESSAGE_IF_NOT_PRODUCTS);
    }

    private Cart getActiveCartByUserName(String username, boolean createNew) {
        return cartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> {
                    if (createNew) {
                        Cart newCart = new Cart();
                        newCart.setUsername(username);
                        newCart.setActive(true);

                        return newCart;
                    } else throw new NotFoundException("Не найдена активная корзина.");
                });
    }

    private void checkProductOnWarehouse(Cart cart) {
        try {
            warehouseClient.checkProductOnWarehouse(cartMapper.toCartDto(cart));
        } catch (NotRequestQuantityProductException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("При обращении к сервису проверки остатка продукта на складе возникла ошибка: " + e.getMessage());
        }
    }
}

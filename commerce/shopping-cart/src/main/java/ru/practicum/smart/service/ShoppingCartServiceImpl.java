package ru.practicum.smart.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.dto.cart.CartDto;
import ru.practicum.smart.dto.cart.NewQuantityProduct;
import ru.practicum.smart.exception.EmptyUsernameException;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.mapper.CartMapper;
import ru.practicum.smart.model.Cart;
import ru.practicum.smart.model.CartProducts;
import ru.practicum.smart.storage.CartRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.smart.util.StringConstants.*;

@Slf4j
@Service
@Qualifier("CartServiceImpl")
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Autowired
    public ShoppingCartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartDto addProducts(String username, Map<String, Integer> products) {
        // Проверка имени пользователя
        checkUserName(username);

        // Проверка наличия пордуктов добавляемых в корзину
        checkProducts(products);

        // Получаем активную карту пользователя, если нет, то создаем
        Cart cart = getActiveCartByUserName(username);

        // Добавляем переданные продукты в корзину
        Map<String, CartProducts> existingProducts = cart.getProducts().stream()
                .collect(Collectors.toMap(CartProducts::getProductId, Function.identity()));

        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productId = entry.getKey();
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

        // todo проверить остаток продуктов на складе

        cart = cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartDto removeProducts(String username, List<String> productIds) {
        // Проверка имени пользователя
        checkUserName(username);

        // Проверка наличия пордуктов добавляемых в корзину
        checkProducts(productIds);

        // Получаем активную карту пользователя, если нет, то создаем
        Cart cart = getActiveCartByUserName(username);

        // Удаляем продукты из корзины
        Set<String> curProductIds = cart.getProducts().stream()
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
        return null;
    }

    @Override
    public CartDto getActiveCart(String username) {
        return null;
    }

    @Override
    public Boolean deactivate(String username) {
        return null;
    }

    private void checkUserName(String username) {
        if (username == null || username.isEmpty())
            throw new EmptyUsernameException(MESSAGE_IF_NOT_NAME_USER, log);
    }

    private void checkProducts(Map<String, Integer> products) {
        if (products == null || products.isEmpty())
            throw new ValidationException(MESSAGE_IF_NOT_PRODUCTS, log);
    }

    private void checkProducts(List<String> products) {
        if (products == null || products.isEmpty())
            throw new ValidationException(MESSAGE_IF_NOT_PRODUCTS, log);
    }

    private Cart getActiveCartByUserName(String username) {
        Cart cart = cartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsername(username);
                    newCart.setActive(true);

                    return newCart;
                });
        return cart;
    }
}

//public class ShoppingCartServiceImpl implements ShoppingCartService {
//    private final ShoppingCartRepository cartRepository;
//    private final ShoppingCartMapper cartMapper;
//    private final WarehouseClient warehouseClient;
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public ShoppingCartDto getActiveShoppingCart(String username) {
//        usernameNotEmptyOrThrow(username);
//        ShoppingCart cart = getActiveCartOrThrow(username);
//        return cartMapper.toDto(cart);
//    }
//
//    @Override
//    @Transactional
//    public Boolean deactivateShoppingCart(String username) {
//        usernameNotEmptyOrThrow(username);
//        ShoppingCart cart = getActiveCartOrThrow(username);
//        cart.setActive(false);
//        cartRepository.save(cart);
//        return true;
//    }
//
//
//    @Override
//    @Transactional
//    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeQuantity) {
//        usernameNotEmptyOrThrow(username);
//        ShoppingCart cart = getActiveCartOrThrow(username);
//
//        if (changeQuantity == null) {
//            throw new IllegalArgumentException("changeQuantity == null");
//        }
//        UUID productId = changeQuantity.getProductId();
//        Integer newQuantity = changeQuantity.getNewQuantity();
//
//        Optional<CartItem> existingItemOpt = cart.getItems().stream()
//                .filter(item -> item.getProductId().equals(productId))
//                .findFirst();
//        if (existingItemOpt.isEmpty()) {
//            throw new ProductNotFoundException("Товар " + productId + " не найден в корзине " + username);
//        }
//
//        existingItemOpt.get().setQuantity(newQuantity);
//
//        warehouseClient.checkAvailabilityForCart(cartMapper.toDto(cart));
//
//        cart = cartRepository.save(cart);
//        return cartMapper.toDto(cart);
//    }
//
//
//    private ShoppingCart getActiveCartOrThrow(String username) {
//        return cartRepository.findByUsernameAndActiveTrue(username).orElseThrow(() ->
//                new CartNotFoundException("Не найдена активная корзина пользователя " + username));
//    }
//
//}
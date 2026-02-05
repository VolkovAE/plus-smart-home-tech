package ru.practicum.smart.util;

public final class StringConstants {
    private StringConstants() {
    }

    public static final String SHEMA_NAME = "shopping_cart";
    public static final String TABLE_NAME_ENTITY_CART = "Carts";
    public static final String TABLE_NAME_ENTITY_CART_PRODUCTS = "Cart_Product";
    public static final String COLUMN_NAME_ENTITY_CART_ID = "cart_id";
    public static final String COLUMN_NAME_ENTITY_CART_USERNAME = "username";
    public static final String COLUMN_NAME_ENTITY_CART_ACTIVE = "active";
    public static final String COLUMN_NAME_ENTITY_CART_PRODUCT_PRODUCT_ID = "product_id";
    public static final String MESSAGE_IF_NOT_NAME_USER = "Имя пользователя не должно быть пустым.";
    public static final String MESSAGE_IF_NOT_PRODUCTS = "Не указан список продуктов.";
    public static final String MESSAGE_IF_NOT_PRODUCTS_IN_CARD = "Нет удаляемых продуктов (а) в корзине.";
}

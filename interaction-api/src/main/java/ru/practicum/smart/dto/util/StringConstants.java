package ru.practicum.smart.dto.util;

public final class StringConstants {
    private StringConstants() {
    }

    public static final String NAME_SERVICE_SHOPPING_CART = "shopping-cart";
    public static final String PATH_SHOPPING_CART = "/api/v1/shopping-cart";
    public static final String PATH_SHOPPING_CART_REMOVE = "/remove";
    public static final String PATH_SHOPPING_CHANGE_QUANTITY = "/change-quantity";
    public static final String NAME_SERVICE_SHOPPING_STORE = "shopping-store";
    public static final String PATH_SHOPPING_STORE = "/api/v1/shopping-store";
    public static final String PATH_SHOPPING_STORE_PRODUCT_ID = "/{productId}";
    public static final String PATH_SHOPPING_STORE_CATEGORY = "category";
    public static final String PATH_SHOPPING_STORE_REMOVE = "/removeProductFromStore";
    public static final String PATH_SHOPPING_STORE_QUANTITY_STATE = "/quantityState";
    public static final String NAME_SERVICE_WAREHOUSE = "warehouse";
    public static final String PATH_WAREHOUSE = "/api/v1/warehouse";
    public static final String PATH_WAREHOUSE_ADD = "/add";
    public static final String PATH_WAREHOUSE_ADDRESS = "/address";
    public static final String PATH_WAREHOUSE_CHECK = "/check";
    public static final String PATH_SHOPPING_ORDER = "/api/v1/order";
    public static final String NAME_SERVICE_ORDER = "order";
    public static final String PATH_ORDER_RETURN = "/return";
    public static final String PATH_ORDER_PAYMENT = "/payment";
    public static final String PATH_ORDER_PAYMENT_FAILED = "/payment/failed";
    public static final String PATH_ORDER_DELIVERY = "/delivery";
    public static final String PATH_ORDER_DELIVERY_FAILED = "/delivery/failed";
    public static final String PATH_ORDER_COMPLECTED = "/completed";
    public static final String PATH_ORDER_CALCULATE_TOTAL = "/calculate/total";
    public static final String PATH_ORDER_CALCULATE_DELIVERY = "/calculate/delivery";
    public static final String PATH_ORDER_ASSEMBLY = "/assembly";
    public static final String PATH_ORDER_ASSEMBLY_FAILED = "/assembly/failed";
    public static final String REQUESTPARAM_ORDER_USERNAME = "username";
    public static final String NAME_SERVICE_PAYMENT = "payment";
    public static final String PATH_PAYMENT = "/api/v1/payment";
    public static final String PATH_ORDER_TOTAL_COST = "/totalCost";
    public static final String PATH_ORDER_REFUND = "/refund";
    public static final String PATH_ORDER_PRODUCT_COST = "/productCost";
    public static final String PATH_ORDER_FAILED = "/failed";
    public static final String NAME_SERVICE_DELIVERY = "delivery";
    public static final String PATH_DELIVERY = "/api/v1/delivery";
    public static final String PATH_DELIVERY_SUCCESSFUL = "/{orderId}/successful";
    public static final String PATH_DELIVERY_PICKED = "/{orderId}/picked";
    public static final String PATH_DELIVERY_FAILED = "/{orderId}/failed";
    public static final String PATH_DELIVERY_COST = "/cost";
}

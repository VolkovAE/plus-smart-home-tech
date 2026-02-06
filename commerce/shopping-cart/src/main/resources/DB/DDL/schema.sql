;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS shopping_cart;

CREATE TABLE IF NOT EXISTS shopping_cart.Carts (
    cart_id UUID PRIMARY KEY,
    username VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS shopping_cart.cart_product (
    cart_id UUID NOT NULL REFERENCES shopping_cart.carts(cart_id) ON DELETE CASCADE,
    product_id VARCHAR(36) NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (cart_id, product_id)
);

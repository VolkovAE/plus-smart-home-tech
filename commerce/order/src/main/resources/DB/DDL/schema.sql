;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS order_;

CREATE TABLE IF NOT EXISTS order_.orders (
    order_id        UUID PRIMARY KEY,
    cart_id         UUID NOT NULL,
    payment_id      UUID,
    delivery_id     UUID,
    order_state     VARCHAR(25),
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile         BOOLEAN,
    total_price     DECIMAL,
    delivery_price  DECIMAL,
    product_price   DECIMAL,
    user_name       VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS order_.order_items (
    id          UUID PRIMARY KEY,
    order_id    UUID NOT NULL REFERENCES order_.orders(order_id) ON DELETE CASCADE,
    product_id  UUID NOT NULL,
    quantity    INTEGER NOT NULL
);
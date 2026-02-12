;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.products
(
    product_Id UUID PRIMARY KEY,
    depth NUMERIC(15,2),
    height NUMERIC(15,2),
    width NUMERIC(15,2),
    quantity INTEGER NOT NULL DEFAULT 0,
    weight NUMERIC(15,3),
    fragile BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS warehouse.bookings
(
    booking_id      UUID PRIMARY KEY,
    delivery_id     UUID,
    order_id        UUID,
    delivery_weight NUMERIC(15,3),
    delivery_volume NUMERIC(15,3),
    fragile         BOOLEAN
);

CREATE TABLE IF NOT EXISTS warehouse.booking_products (
    id          UUID PRIMARY KEY,
    booking_id  UUID NOT NULL,
    product_id  UUID NOT NULL,
    quantity    INTEGER NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES warehouse.bookings(booking_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES warehouse.products(product_id),
    UNIQUE (booking_id, product_id)
);
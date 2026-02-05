;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.products
(
    product_Id VARCHAR(36) PRIMARY KEY,
    depth NUMERIC(15,2),
    height NUMERIC(15,2),
    width NUMERIC(15,2),
    quantity INTEGER NOT NULL DEFAULT 0,
    weight NUMERIC(15,3),
    fragile BOOLEAN DEFAULT FALSE
);
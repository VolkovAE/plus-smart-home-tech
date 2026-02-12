;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS delivery;

CREATE TABLE IF NOT EXISTS delivery.deliveries (
    delivery_id     UUID PRIMARY KEY,
    delivery_state  VARCHAR(25) NOT NULL,
    order_id        UUID NOT NULL,
    from_country    VARCHAR(150),
    from_city       VARCHAR(150),
    from_street     VARCHAR(150),
    from_house      VARCHAR(150),
    from_flat       VARCHAR(150),
    to_country      VARCHAR(150),
    to_city         VARCHAR(150),
    to_street       VARCHAR(150),
    to_house        VARCHAR(150),
    to_flat         VARCHAR(150)
);

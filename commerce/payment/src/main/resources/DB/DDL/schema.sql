;
--одна команда должны быть, если не будет запросов'
CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment.payments (
    payment_id      UUID PRIMARY KEY,
    order_id        UUID,
    state           VARCHAR(10),
    total_payment   NUMERIC(15,2),
    delivery_total  NUMERIC(15,2),
    fee_total       NUMERIC(15,2)
);

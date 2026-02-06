package ru.practicum.smart.exception;

import org.slf4j.Logger;

public class NotRequestQuantityProductException extends RuntimeException {
    public NotRequestQuantityProductException(String message) {
        super(message);
    }

    public NotRequestQuantityProductException(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}

package ru.practicum.smart.exception;

public class NotRequestQuantityProductException extends RuntimeException {
    public NotRequestQuantityProductException(String message) {
        super(message);
    }
}

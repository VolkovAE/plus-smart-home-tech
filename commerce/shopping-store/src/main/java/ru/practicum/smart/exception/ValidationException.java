package ru.practicum.smart.exception;

import org.slf4j.Logger;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

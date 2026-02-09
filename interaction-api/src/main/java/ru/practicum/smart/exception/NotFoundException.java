package ru.practicum.smart.exception;

import org.slf4j.Logger;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

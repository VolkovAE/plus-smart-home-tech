package ru.practicum.smart.exception;

import org.slf4j.Logger;

public class EmptyUsernameException extends RuntimeException {
    public EmptyUsernameException(String message) {
        super(message);
    }

    public EmptyUsernameException(String message, Logger logger) {
        this(message);
        logger.error(message);
    }
}

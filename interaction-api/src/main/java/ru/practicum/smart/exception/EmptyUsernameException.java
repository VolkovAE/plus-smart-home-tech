package ru.practicum.smart.exception;

public class EmptyUsernameException extends RuntimeException {
    public EmptyUsernameException(String message) {
        super(message);
    }
}

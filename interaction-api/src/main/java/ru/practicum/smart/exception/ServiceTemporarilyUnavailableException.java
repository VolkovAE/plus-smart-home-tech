package ru.practicum.smart.exception;

public class ServiceTemporarilyUnavailableException extends RuntimeException {
    public ServiceTemporarilyUnavailableException(String message) {
        super(message);
    }
}

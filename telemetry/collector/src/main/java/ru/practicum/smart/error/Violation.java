package ru.practicum.smart.error;

/**
 * @param fieldName имя поля не прошедшее валидацию
 * @param message   сообщение о нарушении валидации
 */
public record Violation(String fieldName, String message) {
}

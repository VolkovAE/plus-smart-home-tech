package ru.practicum.smart.error;

import java.util.List;

/**
 * @param violations список нарушений
 */
public record ValidationErrorResponse(List<Violation> violations) {
}

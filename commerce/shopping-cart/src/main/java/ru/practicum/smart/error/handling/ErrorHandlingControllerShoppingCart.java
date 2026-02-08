package ru.practicum.smart.error.handling;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.smart.exception.EmptyUsernameException;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.handling.ErrorResponse;
import ru.practicum.smart.handling.ValidationErrorResponse;
import ru.practicum.smart.handling.Violation;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerShoppingCart {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingControllerShoppingCart.class);

    /**
     * Отлавливаю исключения типа ConstraintViolationException, ошибка в параметрах запроса, параметрах пути.
     *
     * @param e - исключение типа ConstraintViolationException
     * @return - возвращаем объект класса ValidationErrorResponse со списком нарушений
     */

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        log.error("Ошибка валидации: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    /**
     * Отлавливаю исключения типа MethodArgumentNotValidException, объект не прошел валидацию.
     *
     * @param e - исключение типа ConstraintViolationException
     * @return - возвращаем объект класса ValidationErrorResponse со списком нарушений
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // проверяем, наличие ошибок при валидации значений в полях объекта
        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        // если ошибок валидации значений в полях объекта нет, но исключение выброшено, значит валидация уровня типа (класса)
        if (violations.isEmpty()) {
            violations = e.getAllErrors().stream()
                    .map(error -> new Violation("-", error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        log.error("Объект не прошел валидацию: {}", violations);

        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler({ValidationException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onValidationException(Exception e) {
        Violation violation = new Violation("-", e.getMessage());
        List<Violation> violationList = List.of(violation);

        return new ValidationErrorResponse(violationList);
    }

    @ExceptionHandler(EmptyUsernameException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ValidationErrorResponse onEmptyUsernameException(EmptyUsernameException e) {
        Violation violation = new Violation("-", e.getMessage());
        List<Violation> violationList = List.of(violation);

        return new ValidationErrorResponse(violationList);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeException(RuntimeException e) {
        return new ErrorResponse("Произошла непредвиденная ошибка. Информация: " + e.getMessage() + ".");
    }
}

package ru.practicum.smart.handling.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.practicum.smart.exception.InternalServerErrorException;
import ru.practicum.smart.exception.NotFoundServiceException;
import ru.practicum.smart.exception.ServiceTemporarilyUnavailableException;

public class CustomErrorDecoder implements ErrorDecoder {
    // используем стандартный декодер для всех кодов, которые не обработаем явно
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // обработка ошибки 404 (Not Found)
        if (response.status() == 404)
            return new NotFoundServiceException("Ресурс не найден для метода: " + methodKey);

        // обработка ошибки 500 (Internal Server Error)
        if (response.status() == 500)
            return new InternalServerErrorException("Ошибка на сервере при вызове метода: " + methodKey);

        // обработка ошибки 503 (Service Temporarily Unavailable)
        if (response.status() == 503)
            return new ServiceTemporarilyUnavailableException("Сервис " + methodKey + " временно не доступен.");

        // для других кодов используем стандартное поведение
        return defaultDecoder.decode(methodKey, response);
    }
}

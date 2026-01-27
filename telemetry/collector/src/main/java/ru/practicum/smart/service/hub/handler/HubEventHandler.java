package ru.practicum.smart.service.hub.handler;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

// Интерфейс, объединяющий все хендлеры для HubEventProto-событий.
// Благодаря ему мы сможем внедрить все хендлеры в виде списка
// в компонент, который будет распределять получаемые события по
// их обработчикам
public interface HubEventHandler {
    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}

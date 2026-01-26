package ru.practicum.smart.service.hub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.hub.HubEvent;
import ru.practicum.smart.model.hub.HubEventType;
import ru.practicum.smart.model.hub.device.DeviceAddedEvent;
import ru.practicum.smart.service.hub.HubService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Component
public class DeviceAddedEventHandler implements HubEventHandler {
    private final HubService hubService;

    private static final Logger log = LoggerFactory.getLogger(DeviceAddedEventHandler.class);

    @Autowired
    public DeviceAddedEventHandler(@Qualifier("HubServiceImpl") HubService hubService) {
        this.hubService = hubService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) HubEvent.getHubEventFromProto(event, HubEventType.DEVICE_ADDED);

        hubService.toCollect(deviceAddedEvent);
    }
}

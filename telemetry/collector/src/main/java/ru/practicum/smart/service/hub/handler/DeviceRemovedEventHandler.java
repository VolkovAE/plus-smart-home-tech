package ru.practicum.smart.service.hub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.hub.HubEvent;
import ru.practicum.smart.model.hub.HubEventType;
import ru.practicum.smart.model.hub.device.DeviceRemovedEvent;
import ru.practicum.smart.service.hub.HubService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

@Component
public class DeviceRemovedEventHandler implements HubEventHandler {
    private final HubService hubService;

    private static final Logger log = LoggerFactory.getLogger(DeviceRemovedEventHandler.class);

    @Autowired
    public DeviceRemovedEventHandler(@Qualifier("HubServiceImpl") HubService hubService) {
        this.hubService = hubService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) HubEvent.getHubEventFromProto(event, HubEventType.DEVICE_REMOVED);

        hubService.toCollect(deviceRemovedEvent);
    }
}

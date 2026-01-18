package ru.practicum.smart.service.hub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.hub.device.DeviceRemovedEvent;
import ru.practicum.smart.service.hub.HubService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.time.Instant;

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
        DeviceRemovedEvent deviceRemovedEvent = new DeviceRemovedEvent();
        deviceRemovedEvent.setHubId(event.getHubId());
        deviceRemovedEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        deviceRemovedEvent.setId(event.getDeviceRemoved().getId());

        hubService.toCollect(deviceRemovedEvent);
    }
}

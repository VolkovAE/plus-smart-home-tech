package ru.practicum.smart.service.hub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.mapper.DeviceTypeMapper;
import ru.practicum.smart.model.hub.device.DeviceAddedEvent;
import ru.practicum.smart.service.hub.HubService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.time.Instant;

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
        DeviceAddedEvent deviceAddedEvent = new DeviceAddedEvent();
        deviceAddedEvent.setHubId(event.getHubId());
        deviceAddedEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        deviceAddedEvent.setId(event.getDeviceAdded().getId());
        deviceAddedEvent.setDeviceType(DeviceTypeMapper.mapToDeviceType(event.getDeviceAdded().getType()));

        hubService.toCollect(deviceAddedEvent);
    }
}

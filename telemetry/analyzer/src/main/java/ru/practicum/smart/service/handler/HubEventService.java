package ru.practicum.smart.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.service.DeviceService;
import ru.practicum.smart.service.ScenarioService;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Service
public class HubEventService {
    private static final Logger log = LoggerFactory.getLogger(HubEventService.class);

    private final DeviceService deviceService;
    private final ScenarioService scenarioService;

    @Autowired
    public HubEventService(DeviceService deviceService,
                           ScenarioService scenarioService) {
        this.deviceService = deviceService;
        this.scenarioService = scenarioService;
    }

    public void handleRecord(HubEventAvro hubEventAvro) {
        String hubId = hubEventAvro.getHubId();
        Object payload = hubEventAvro.getPayload();
        if (payload == null) return;

        switch (payload) {
            case DeviceAddedEventAvro event -> deviceService.addDevice(event, hubId);
            case DeviceRemovedEventAvro event -> deviceService.removeDevice(event, hubId);
            case ScenarioAddedEventAvro event -> scenarioService.addScenario(event, hubId);
            case ScenarioRemovedEventAvro event -> scenarioService.removeScenario(event, hubId);
            default -> log.error("Неизвестный тип payload: {}", payload.getClass().getName());
        }
    }
}

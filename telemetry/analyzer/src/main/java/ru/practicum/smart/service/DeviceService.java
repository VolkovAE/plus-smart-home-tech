package ru.practicum.smart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.smart.model.Sensor;
import ru.practicum.smart.storage.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Service
public class DeviceService {
    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final SensorRepository sensorRepository;

    public DeviceService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void addDevice(DeviceAddedEventAvro eventAvro, String hubId) {
        String id = eventAvro.getId();

        log.info("Запрос на добавление сенсора с id = {} в хабе с hubId = {}", id, hubId);

        if (sensorRepository.findByIdAndHubId(id, hubId).isPresent()) return;

        Sensor sensor = new Sensor();
        sensor.setId(eventAvro.getId());
        sensor.setHubId(hubId);
        sensorRepository.save(sensor);

        log.info("Сенсор с id = {} добавлен.", id);
    }

    @Transactional
    public void removeDevice(DeviceRemovedEventAvro eventAvro, String hubId) {
        String id = eventAvro.getId();
        if (sensorRepository.findByIdAndHubId(id, hubId).isEmpty()) return;

        sensorRepository.deleteById(id);

        log.info("Сенсор с id = {} удален из хаба с hubId = {}.", id, hubId);
    }
}

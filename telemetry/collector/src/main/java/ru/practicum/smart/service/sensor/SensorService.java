package ru.practicum.smart.service.sensor;

import ru.practicum.smart.model.sensor.SensorEvent;

public interface SensorService {
    void toCollect(SensorEvent event);
}

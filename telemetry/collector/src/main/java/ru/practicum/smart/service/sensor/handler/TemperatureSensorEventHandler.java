package ru.practicum.smart.service.sensor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.sensor.TemperatureSensorEvent;
import ru.practicum.smart.service.sensor.SensorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

@Component
public class TemperatureSensorEventHandler implements SensorEventHandler {
    private final SensorService sensorService;

    private static final Logger log = LoggerFactory.getLogger(TemperatureSensorEventHandler.class);

    @Autowired
    public TemperatureSensorEventHandler(@Qualifier("SensorServiceImpl") SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        TemperatureSensorEvent temperatureSensorEvent = new TemperatureSensorEvent();
        temperatureSensorEvent.setId(event.getId());
        temperatureSensorEvent.setHubId(event.getHubId());
        temperatureSensorEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        temperatureSensorEvent.setTemperatureC(event.getTemperatureSensor().getTemperatureC());
        temperatureSensorEvent.setTemperatureF(event.getTemperatureSensor().getTemperatureF());

        sensorService.toCollect(temperatureSensorEvent);
    }
}

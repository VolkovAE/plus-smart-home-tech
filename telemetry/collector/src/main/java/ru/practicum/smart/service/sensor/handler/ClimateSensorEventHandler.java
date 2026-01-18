package ru.practicum.smart.service.sensor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.sensor.ClimateSensorEvent;
import ru.practicum.smart.service.sensor.SensorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    private final SensorService sensorService;

    private static final Logger log = LoggerFactory.getLogger(ClimateSensorEventHandler.class);

    @Autowired
    public ClimateSensorEventHandler(@Qualifier("SensorServiceImpl") SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        ClimateSensorEvent climateSensorEvent = new ClimateSensorEvent();
        climateSensorEvent.setId(event.getId());
        climateSensorEvent.setHubId(event.getHubId());
        climateSensorEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        climateSensorEvent.setTemperatureC(event.getClimateSensor().getTemperatureC());
        climateSensorEvent.setHumidity(event.getClimateSensor().getHumidity());
        climateSensorEvent.setCo2Level(event.getClimateSensor().getCo2Level());

        sensorService.toCollect(climateSensorEvent);
    }
}

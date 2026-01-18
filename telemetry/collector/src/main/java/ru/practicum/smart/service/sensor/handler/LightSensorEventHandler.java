package ru.practicum.smart.service.sensor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.sensor.LightSensorEvent;
import ru.practicum.smart.service.sensor.SensorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

@Component
public class LightSensorEventHandler implements SensorEventHandler {
    private final SensorService sensorService;

    private static final Logger log = LoggerFactory.getLogger(LightSensorEventHandler.class);

    @Autowired
    public LightSensorEventHandler(@Qualifier("SensorServiceImpl") SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        LightSensorEvent lightSensorEvent = new LightSensorEvent();
        lightSensorEvent.setId(event.getId());
        lightSensorEvent.setHubId(event.getHubId());
        lightSensorEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        lightSensorEvent.setLinkQuality(event.getLightSensor().getLinkQuality());
        lightSensorEvent.setLuminosity(event.getLightSensor().getLuminosity());

        sensorService.toCollect(lightSensorEvent);
    }
}

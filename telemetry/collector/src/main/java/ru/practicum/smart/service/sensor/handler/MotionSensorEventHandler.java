package ru.practicum.smart.service.sensor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.model.sensor.MotionSensorEvent;
import ru.practicum.smart.service.sensor.SensorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {
    private final SensorService sensorService;

    private static final Logger log = LoggerFactory.getLogger(MotionSensorEventHandler.class);

    @Autowired
    public MotionSensorEventHandler(@Qualifier("SensorServiceImpl") SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        MotionSensorEvent motionSensorEvent = new MotionSensorEvent();
        SensorEventHandler.setGeneralField(motionSensorEvent, event);
        motionSensorEvent.setMotion(event.getMotionSensor().getMotion());
        motionSensorEvent.setVoltage(event.getMotionSensor().getVoltage());
        motionSensorEvent.setLinkQuality(event.getMotionSensor().getLinkQuality());

        sensorService.toCollect(motionSensorEvent);
    }
}

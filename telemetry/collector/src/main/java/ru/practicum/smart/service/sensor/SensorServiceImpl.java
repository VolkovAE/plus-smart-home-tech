package ru.practicum.smart.service.sensor;

import lombok.extern.java.Log;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.config.KafkaTopicProperties;
import ru.practicum.smart.model.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_PRODUCER_KAFKA_TELEMETRY;

@Service
@Qualifier("SensorServiceImpl")
@Log
public class SensorServiceImpl implements SensorService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Autowired
    public SensorServiceImpl(@Qualifier(BEAN_NAME_PRODUCER_KAFKA_TELEMETRY) Producer<String, SpecificRecordBase> producer,
                             KafkaTopicProperties kafkaTopicProperties) {
        this.producer = producer;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    @Override
    public void toCollect(SensorEvent event) {
        // сервис кодирования сообщения датчика сенсора в формате Avro и размещение в кафку

        // получаем объект сгенерированного класса на базе объекта с данными от датчика для сериализации
        SpecificRecordBase eventAvro = getSensorEventAvro(event);

        // в качестве данных сообщения указываем экземпляр SensorEventAvro
        // перед отправкой брокеру Kafka-продюсер сам вызовет сериализатор
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(kafkaTopicProperties.sensors(), null, event.getTimestamp().toEpochMilli(), null, eventAvro);

        // отправляем данные
        producer.send(record);
    }

    private SensorEventAvro getSensorEventAvro(SensorEvent event) {
        Object payload = null;
        if (event.getType() == SensorEventType.LIGHT_SENSOR_EVENT) {
            payload = new LightSensorAvro(((LightSensorEvent) event).getLinkQuality(), ((LightSensorEvent) event).getLuminosity());
        } else if (event.getType() == SensorEventType.CLIMATE_SENSOR_EVENT) {
            payload = new ClimateSensorAvro(((ClimateSensorEvent) event).getTemperatureC(), ((ClimateSensorEvent) event).getHumidity(), ((ClimateSensorEvent) event).getCo2Level());
        } else if (event.getType() == SensorEventType.MOTION_SENSOR_EVENT) {
            payload = new MotionSensorAvro(((MotionSensorEvent) event).getLinkQuality(), ((MotionSensorEvent) event).isMotion(), ((MotionSensorEvent) event).getVoltage());
        } else if (event.getType() == SensorEventType.SWITCH_SENSOR_EVENT) {
            payload = new SwitchSensorAvro(((SwitchSensorEvent) event).isState());
        } else if (event.getType() == SensorEventType.TEMPERATURE_SENSOR_EVENT) {
            payload = new TemperatureSensorAvro(((TemperatureSensorEvent) event).getTemperatureC(), ((TemperatureSensorEvent) event).getTemperatureF());
        }

        SensorEventAvro sensorEventAvro = new SensorEventAvro();
        sensorEventAvro.setId(event.getId());
        sensorEventAvro.setHubId(event.getHubId());
        sensorEventAvro.setTimestamp(event.getTimestamp());
        sensorEventAvro.setPayload(payload);

        return sensorEventAvro;
    }
}

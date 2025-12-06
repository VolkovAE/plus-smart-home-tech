package ru.practicum.smart.service.sensor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.model.sensor.SensorEvent;

@Service
@Qualifier("SensorServiceImpl")
public class SensorServiceImpl implements SensorService {
    @Override
    public void toCollect(SensorEvent event) {
        // реализовать сервис кодирования сообщения датчика сенсора в формате Avro и размещение в кафку
    }
}

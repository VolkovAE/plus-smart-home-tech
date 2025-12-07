package ru.practicum.smart.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.model.hub.device.DeviceType;

@Component
public class StringToDeviceTypeConverter extends StdConverter<String, DeviceType> {
    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(StringToDeviceTypeConverter.class);

    @Override
    public DeviceType convert(String value) {
        if (value.equals("MOTION_SENSOR")) return DeviceType.MOTION_SENSOR;
        else if (value.equals("TEMPERATURE_SENSOR")) return DeviceType.TEMPERATURE_SENSOR;
        else if (value.equals("LIGHT_SENSOR")) return DeviceType.LIGHT_SENSOR;
        else if (value.equals("CLIMATE_SENSOR")) return DeviceType.CLIMATE_SENSOR;
        else if (value.equals("SWITCH_SENSOR")) return DeviceType.SWITCH_SENSOR;
        else throw new ValidationException("Неизвестный тип устройства: " + value, log);
    }
}

package ru.practicum.smart.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemperatureSensorEvent extends SensorEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @Description("Температура в градусах Цельсия")
    int temperatureC;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @Description("Температура в градусах Фаренгейта")
    int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}

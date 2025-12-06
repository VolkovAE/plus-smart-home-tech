package ru.practicum.smart.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
public class ClimateSensorEvent extends SensorEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Уровень температуры по шкале Цельсия")
    int temperatureC;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Positive(message = "Значение влажности не может быть меньше или равна нулю")
    @Description("Влажность")
    int humidity;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @PositiveOrZero(message = "Значение уровня CO2 не может быть меньше нуля")
    @Description("Уровень CO2")
    int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}

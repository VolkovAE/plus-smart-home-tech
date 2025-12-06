package ru.practicum.smart.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class LightSensorEvent extends SensorEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @PositiveOrZero(message = "Значение качества связи не может быть меньше нуля")
    @Description("Качество связи")
    int linkQuality;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @PositiveOrZero(message = "Значение уровня освещенности не может быть меньше нуля")
    @Description("Уровень освещенности")
    int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}

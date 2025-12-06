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
public class MotionSensorEvent extends SensorEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Качество связи")
    int linkQuality;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Наличие/отсутствие движения")
    boolean motion;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Напряжение")
    int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}

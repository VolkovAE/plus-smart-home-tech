package ru.practicum.smart.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = SensorEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_SENSOR_EVENT")
})
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class SensorEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор события датчика")
    @Description("Идентификатор события датчика")
    String id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор хаба, связанного с событием датчика")
    @Description("Идентификатор хаба, связанного с событием")
    String hubId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Временная метка события. По умолчанию устанавливается текущее время.")
    Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}

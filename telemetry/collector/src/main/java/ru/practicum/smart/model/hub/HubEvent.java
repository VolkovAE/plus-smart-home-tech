package ru.practicum.smart.model.hub;

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
import ru.practicum.smart.model.hub.device.DeviceAddedEvent;
import ru.practicum.smart.model.hub.device.DeviceRemovedEvent;
import ru.practicum.smart.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.smart.model.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class HubEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор хаба, связанного с событием хаба")
    @Description("Идентификатор хаба, связанный с событием")
    String hubId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Временная метка события. По умолчанию устанавливается текущее время.")
    Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}

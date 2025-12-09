package ru.practicum.smart.model.hub.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.model.hub.HubEvent;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class DeviceEvent extends HubEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор добавленного / удаленного устройства")
    @Description("Идентификатор добавленного / удаленного устройства")
    String id;
}

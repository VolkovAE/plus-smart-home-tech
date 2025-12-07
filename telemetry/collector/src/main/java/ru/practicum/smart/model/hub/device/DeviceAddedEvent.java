package ru.practicum.smart.model.hub.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.converter.StringToDeviceTypeConverter;
import ru.practicum.smart.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Событие, сигнализирующее о добавлении нового устройства в систему")
public class DeviceAddedEvent extends DeviceEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @JsonDeserialize(converter = StringToDeviceTypeConverter.class)
    @Description("Тип устройства, которое добавляем в систему.")
    DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}

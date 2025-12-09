package ru.practicum.smart.model.hub.device;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Description;
import ru.practicum.smart.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Событие, сигнализирующее о удалении устройства из системы")
public class DeviceRemovedEvent extends DeviceEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}

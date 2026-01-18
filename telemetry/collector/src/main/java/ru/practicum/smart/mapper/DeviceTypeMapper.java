package ru.practicum.smart.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.model.hub.device.DeviceType;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeviceTypeMapper {
    public static DeviceType mapToDeviceType(DeviceTypeProto deviceTypeProto) {
        if (deviceTypeProto == DeviceTypeProto.MOTION_SENSOR) return DeviceType.MOTION_SENSOR;
        else if (deviceTypeProto == DeviceTypeProto.CLIMATE_SENSOR) return DeviceType.CLIMATE_SENSOR;
        else if (deviceTypeProto == DeviceTypeProto.TEMPERATURE_SENSOR) return DeviceType.TEMPERATURE_SENSOR;
        else if (deviceTypeProto == DeviceTypeProto.LIGHT_SENSOR) return DeviceType.LIGHT_SENSOR;
        else if (deviceTypeProto == DeviceTypeProto.SWITCH_SENSOR) return DeviceType.SWITCH_SENSOR;
        else throw new NotFoundException("Не определен тип сенсора: " + deviceTypeProto.toString());
    }
}

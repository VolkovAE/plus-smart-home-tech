package ru.practicum.smart.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.model.hub.scenario.condition.ConditionType;
import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConditionTypeMapper {
    public static ConditionType mapToConditionType(ConditionTypeProto conditionTypeProto) {
        if (conditionTypeProto == ConditionTypeProto.MOTION) return ConditionType.MOTION;
        else if (conditionTypeProto == ConditionTypeProto.LUMINOSITY) return ConditionType.LUMINOSITY;
        else if (conditionTypeProto == ConditionTypeProto.SWITCH) return ConditionType.SWITCH;
        else if (conditionTypeProto == ConditionTypeProto.TEMPERATURE) return ConditionType.TEMPERATURE;
        else if (conditionTypeProto == ConditionTypeProto.CO2LEVEL) return ConditionType.CO2LEVEL;
        else if (conditionTypeProto == ConditionTypeProto.HUMIDITY) return ConditionType.HUMIDITY;
        else throw new NotFoundException("Не определен тип условия сценария: " + conditionTypeProto.toString());
    }
}

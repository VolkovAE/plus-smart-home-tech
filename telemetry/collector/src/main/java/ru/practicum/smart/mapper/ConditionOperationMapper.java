package ru.practicum.smart.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.model.hub.scenario.condition.ConditionOperation;
import ru.yandex.practicum.grpc.telemetry.event.ConditionOperationProto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConditionOperationMapper {
    public static ConditionOperation mapToConditionOperation(ConditionOperationProto conditionOperationProto) {
        if (conditionOperationProto == ConditionOperationProto.EQUALS) return ConditionOperation.EQUALS;
        else if (conditionOperationProto == ConditionOperationProto.GREATER_THAN)
            return ConditionOperation.GREATER_THAN;
        else if (conditionOperationProto == ConditionOperationProto.LOWER_THAN) return ConditionOperation.LOWER_THAN;
        else
            throw new NotFoundException("Не определен тип равенства (<,=,>) в условии сценария: " + conditionOperationProto.toString());
    }
}

package ru.practicum.smart.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.model.hub.scenario.action.ActionType;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionTypeMapper {
    public static ActionType mapToActionType(ActionTypeProto actionTypeProto) {
        if (actionTypeProto == ActionTypeProto.ACTIVATE) return ActionType.ACTIVATE;
        else if (actionTypeProto == ActionTypeProto.DEACTIVATE) return ActionType.DEACTIVATE;
        else if (actionTypeProto == ActionTypeProto.INVERSE) return ActionType.INVERSE;
        else if (actionTypeProto == ActionTypeProto.SET_VALUE) return ActionType.SET_VALUE;
        else throw new NotFoundException("Не определен тип действия в сценарии: " + actionTypeProto.toString());
    }
}

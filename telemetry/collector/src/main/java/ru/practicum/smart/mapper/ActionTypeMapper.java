package ru.practicum.smart.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.smart.exception.NotFoundException;
import ru.practicum.smart.model.hub.scenario.action.ActionType;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionTypeMapper {
    private static final HashMap<ActionTypeProto, ActionType> map = new HashMap<>();

    static {
        map.put(ActionTypeProto.ACTIVATE, ActionType.ACTIVATE);
        map.put(ActionTypeProto.DEACTIVATE, ActionType.DEACTIVATE);
        map.put(ActionTypeProto.INVERSE, ActionType.INVERSE);
        map.put(ActionTypeProto.SET_VALUE, ActionType.SET_VALUE);
    }

    public static ActionType mapToActionType(ActionTypeProto actionTypeProto) {
        ActionType actionType = map.get(actionTypeProto);
        if (actionType == null)
            throw new NotFoundException("Не определен тип действия в сценарии: " + actionTypeProto.toString());

        return actionType;
    }
}

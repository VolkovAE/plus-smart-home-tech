package ru.practicum.smart.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.model.hub.scenario.action.ActionType;

@Component
public class StringToActionTypeConverter extends StdConverter<String, ActionType> {
    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(StringToActionTypeConverter.class);

    @Override
    public ActionType convert(String value) {
        if (value.equals("ACTIVATE")) return ActionType.ACTIVATE;
        else if (value.equals("DEACTIVATE")) return ActionType.DEACTIVATE;
        else if (value.equals("INVERSE")) return ActionType.INVERSE;
        else if (value.equals("SET_VALUE")) return ActionType.SET_VALUE;
        else throw new ValidationException("Неизвестный тип действия сценария: " + value, log);
    }
}

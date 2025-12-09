package ru.practicum.smart.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.model.hub.scenario.condition.ConditionOperation;

@Component
public class StringToConditionOperationConverter extends StdConverter<String, ConditionOperation> {
    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(StringToConditionOperationConverter.class);

    @Override
    public ConditionOperation convert(String value) {
        if (value.equals("EQUALS")) return ConditionOperation.EQUALS;
        else if (value.equals("GREATER_THAN")) return ConditionOperation.GREATER_THAN;
        else if (value.equals("LOWER_THAN")) return ConditionOperation.LOWER_THAN;
        else throw new ValidationException("Неизвестный тип операции условия: " + value, log);
    }
}

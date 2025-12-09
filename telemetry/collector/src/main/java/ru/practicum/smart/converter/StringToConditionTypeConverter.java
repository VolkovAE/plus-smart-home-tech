package ru.practicum.smart.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.smart.exception.ValidationException;
import ru.practicum.smart.model.hub.scenario.condition.ConditionType;

@Component
public class StringToConditionTypeConverter extends StdConverter<String, ConditionType> {
    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(StringToConditionTypeConverter.class);

    @Override
    public ConditionType convert(String value) {
        if (value.equals("MOTION")) return ConditionType.MOTION;
        else if (value.equals("LUMINOSITY")) return ConditionType.LUMINOSITY;
        else if (value.equals("SWITCH")) return ConditionType.SWITCH;
        else if (value.equals("TEMPERATURE")) return ConditionType.TEMPERATURE;
        else if (value.equals("CO2LEVEL")) return ConditionType.CO2LEVEL;
        else if (value.equals("HUMIDITY")) return ConditionType.HUMIDITY;
        else throw new ValidationException("Неизвестный тип условия сценария (показателя): " + value, log);
    }
}

package ru.practicum.smart.model.hub.scenario.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.converter.StringToConditionOperationConverter;
import ru.practicum.smart.converter.StringToConditionTypeConverter;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Условие сценария, которое содержит информацию о датчике, типе условия, операции и значении")
public class ScenarioCondition {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор датчика, связанного с условием")
    @Description("Идентификатор датчика, связанного с условием")
    String sensorId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @JsonDeserialize(converter = StringToConditionTypeConverter.class)
    @Description("Тип используемого условия, т.е. какой показатель используем в условии")
    ConditionType type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @JsonDeserialize(converter = StringToConditionOperationConverter.class)
    @Description("Операция используемая в условии, то есть значение показателя больше, равно, меньше")
    ConditionOperation operation;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @Description("Значение, используемое в условии")
    int value;
}

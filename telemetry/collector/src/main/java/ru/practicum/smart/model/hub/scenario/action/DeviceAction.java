package ru.practicum.smart.model.hub.scenario.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.converter.StringToActionTypeConverter;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Представляет действие, которое должно быть выполнено устройством")
public class DeviceAction {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @NotBlank(message = "Не указан идентификатор датчика, связанного с действием")
    @Description("Идентификатор датчика, связанного с действием")
    String sensorId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @JsonDeserialize(converter = StringToActionTypeConverter.class)
    @Description("Типов действия при срабатывании условия активации сценария")
    ActionType type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Description("Дополнительная информация связанная с действием")
    int value;
}

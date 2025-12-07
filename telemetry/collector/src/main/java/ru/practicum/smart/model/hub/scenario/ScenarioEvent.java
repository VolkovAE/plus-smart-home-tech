package ru.practicum.smart.model.hub.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.model.hub.HubEvent;

import static ru.practicum.smart.util.IntConstantsForRequest.SCENARIO_NAME_LENGTH_MAX;
import static ru.practicum.smart.util.IntConstantsForRequest.SCENARIO_NAME_LENGTH_MIN;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ScenarioEvent extends HubEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Не указано название добавленного / удаленного устройства")
    @Size(min = SCENARIO_NAME_LENGTH_MIN, max = SCENARIO_NAME_LENGTH_MAX, message = "Длина названия сценария должна быть не менее 3 и не более  символов.")
    @Description("Название добавленного / удаленного сценария. Должно содержать не менее 3 символов")
    String name;
}

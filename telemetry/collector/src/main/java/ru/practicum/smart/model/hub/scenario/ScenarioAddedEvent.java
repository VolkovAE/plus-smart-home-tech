package ru.practicum.smart.model.hub.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.model.hub.HubEventType;
import ru.practicum.smart.model.hub.scenario.action.DeviceAction;
import ru.practicum.smart.model.hub.scenario.condition.ScenarioCondition;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Событие добавления сценария в систему. Содержит информацию о названии сценария, условиях и действиях")
public class ScenarioAddedEvent extends ScenarioEvent {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @Description("Список условий, которые связаны со сценарием. Не может быть пустым")
    List<ScenarioCondition> conditions = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, required = true)
    @Description("Список действий, которые должны быть выполнены в рамках сценария. Не может быть пустым.")
    List<DeviceAction> actions = new ArrayList<>();

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}

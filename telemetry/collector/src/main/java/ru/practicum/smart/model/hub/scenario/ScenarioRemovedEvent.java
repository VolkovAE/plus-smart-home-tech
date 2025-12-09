package ru.practicum.smart.model.hub.scenario;

import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.smart.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Description("Событие удаления сценария из системы. Содержит информацию о названии удаленного сценария")
public class ScenarioRemovedEvent extends ScenarioEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}

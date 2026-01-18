package ru.practicum.smart.service.hub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.mapper.ActionTypeMapper;
import ru.practicum.smart.mapper.ConditionOperationMapper;
import ru.practicum.smart.mapper.ConditionTypeMapper;
import ru.practicum.smart.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.smart.model.hub.scenario.action.DeviceAction;
import ru.practicum.smart.model.hub.scenario.condition.ScenarioCondition;
import ru.practicum.smart.service.hub.HubService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;

import java.time.Instant;
import java.util.List;

@Component
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final HubService hubService;

    private static final Logger log = LoggerFactory.getLogger(ScenarioAddedEventHandler.class);

    @Autowired
    public ScenarioAddedEventHandler(@Qualifier("HubServiceImpl") HubService hubService) {
        this.hubService = hubService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        // Объект класса *Proto преобразуем в объект класса из пакета model и далее используем ранее реализованную отправку в брокер
        ScenarioAddedEvent scenarioAddedEvent = new ScenarioAddedEvent();
        scenarioAddedEvent.setHubId(event.getHubId());
        scenarioAddedEvent.setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()));   // преобразовать Timestamp в Instant
        scenarioAddedEvent.setName(event.getScenarioAdded().getName());

        // Набор условий активации сценария - conditions
        List<ScenarioCondition> conditions = event.getScenarioAdded().getConditionList().stream()
                .map(this::getScenarioCondition)
                .toList();
        scenarioAddedEvent.setConditions(conditions);

        // Набор действий при активации сценария - actions
        List<DeviceAction> actions = event.getScenarioAdded().getActionList().stream()
                .map(this::getDeviceAction)
                .toList();
        scenarioAddedEvent.setActions(actions);

        hubService.toCollect(scenarioAddedEvent);
    }

    private ScenarioCondition getScenarioCondition(ScenarioConditionProto scenarioConditionProto) {
        ScenarioCondition scenarioCondition = new ScenarioCondition();
        scenarioCondition.setSensorId(scenarioConditionProto.getSensorId());
        scenarioCondition.setType(ConditionTypeMapper.mapToConditionType(scenarioConditionProto.getType()));
        scenarioCondition.setOperation(ConditionOperationMapper.mapToConditionOperation(scenarioConditionProto.getOperation()));

        // value
        if (scenarioConditionProto.getValueCase() == ScenarioConditionProto.ValueCase.BOOL_VALUE)
            scenarioCondition.setValue(scenarioConditionProto.getBoolValue() ? 1 : 0);
        else if (scenarioConditionProto.getValueCase() == ScenarioConditionProto.ValueCase.INT_VALUE)
            scenarioCondition.setValue(scenarioConditionProto.getIntValue());
        else if (scenarioConditionProto.getValueCase() == ScenarioConditionProto.ValueCase.VALUE_NOT_SET)
            scenarioCondition.setValue(0);

        return scenarioCondition;
    }

    private DeviceAction getDeviceAction(DeviceActionProto deviceActionProto) {
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setSensorId(deviceActionProto.getSensorId());
        deviceAction.setType(ActionTypeMapper.mapToActionType(deviceActionProto.getType()));
        deviceAction.setValue(deviceActionProto.getValue());

        return deviceAction;
    }
}

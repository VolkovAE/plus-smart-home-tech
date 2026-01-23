package ru.practicum.smart.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.model.Action;
import ru.practicum.smart.model.Condition;
import ru.practicum.smart.model.Scenario;
import ru.practicum.smart.storage.ScenarioRepository;
import ru.practicum.smart.storage.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Service
public class ScenarioService {
    private static final Logger log = LoggerFactory.getLogger(ScenarioService.class);

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Autowired
    public ScenarioService(SensorRepository sensorRepository,
                           ScenarioRepository scenarioRepository) {
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
    }

    @Transactional
    public void addScenario(ScenarioAddedEventAvro scenarioAddedEventAvro, String hubId) {
        String name = scenarioAddedEventAvro.getName();

        if (scenarioRepository.findByHubIdAndName(hubId, name).isPresent()) return;

        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(name);

        addConditions(scenarioAddedEventAvro, hubId, scenario);

        addActions(scenarioAddedEventAvro, hubId, scenario);

        scenarioRepository.save(scenario);

        log.info("Сценарий {} добавлен.", name);
    }

    private void addConditions(ScenarioAddedEventAvro scenarioAddedEventAvro, String hubId, Scenario scenario) {
        for (ScenarioConditionAvro scenarioConditionAvro : scenarioAddedEventAvro.getConditions()) {
            String sensorId = scenarioConditionAvro.getSensorId();
            if (sensorRepository.findByIdAndHubId(sensorId, hubId).isEmpty()) continue;

            if (!isValidEnum(scenarioConditionAvro.getType(), ConditionTypeAvro.class)) continue;

            if (!isValidEnum(scenarioConditionAvro.getOperation(), ConditionOperationAvro.class)) continue;

            Condition condition = new Condition();
            condition.setType(scenarioConditionAvro.getType());
            condition.setOperation(scenarioConditionAvro.getOperation());
            condition.setValue(mapValue(scenarioConditionAvro.getValue()));

            scenario.addCondition(sensorId, condition);
        }
    }

    private void addActions(ScenarioAddedEventAvro scenarioAddedEventAvro, String hubId, Scenario scenario) {
        for (DeviceActionAvro deviceActionAvro : scenarioAddedEventAvro.getActions()) {
            String sensorId = deviceActionAvro.getSensorId();
            if (sensorRepository.findByIdAndHubId(sensorId, hubId).isEmpty()) continue;

            if (!isValidEnum(deviceActionAvro.getType(), ActionTypeAvro.class)) continue;

            Action action = new Action();
            action.setType(deviceActionAvro.getType());
            action.setValue(deviceActionAvro.getValue());

            scenario.addAction(sensorId, action);
        }
    }

    private <T extends Enum<T>> boolean isValidEnum(Object value, Class<T> enumClass) {
        if (value == null) return false;
        try {
            Enum.valueOf(enumClass, value.toString());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Integer mapValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        else if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        else if (value == null) return null;
        else return null;
    }

    @Transactional
    public void removeScenario(ScenarioRemovedEventAvro eventAvro, String hubId) {
        String name = eventAvro.getName();

        scenarioRepository.findByHubIdAndName(hubId, name).ifPresent(scenarioRepository::delete);

        log.info("Сценарий {} удален.", name);
    }
}

package ru.practicum.smart.service.hub;

import lombok.extern.java.Log;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.config.KafkaTopicProperties;
import ru.practicum.smart.model.hub.HubEvent;
import ru.practicum.smart.model.hub.HubEventType;
import ru.practicum.smart.model.hub.device.DeviceAddedEvent;
import ru.practicum.smart.model.hub.device.DeviceRemovedEvent;
import ru.practicum.smart.model.hub.device.DeviceType;
import ru.practicum.smart.model.hub.scenario.ScenarioAddedEvent;
import ru.practicum.smart.model.hub.scenario.ScenarioRemovedEvent;
import ru.practicum.smart.model.hub.scenario.action.ActionType;
import ru.practicum.smart.model.hub.scenario.action.DeviceAction;
import ru.practicum.smart.model.hub.scenario.condition.ConditionOperation;
import ru.practicum.smart.model.hub.scenario.condition.ConditionType;
import ru.practicum.smart.model.hub.scenario.condition.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_PRODUCER_KAFKA_TELEMETRY;

@Service
@Qualifier("HubServiceImpl")
@Log
public class HubServiceImpl implements HubService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Autowired
    public HubServiceImpl(@Qualifier(BEAN_NAME_PRODUCER_KAFKA_TELEMETRY) Producer<String, SpecificRecordBase> producer,
                          KafkaTopicProperties kafkaTopicProperties) {
        this.producer = producer;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    @Override
    public void toCollect(HubEvent event) {
        // сервис кодирования сообщения хаба в формате Avro и размещение в кафку

        // получаем объект сгенерированного класса на базе объекта с данными от хаба для сериализации
        SpecificRecordBase eventAvro = getHubEventAvro(event);

        // в качестве данных сообщения указываем экземпляр HubEventAvro
        // перед отправкой брокеру Kafka-продюсер сам вызовет сериализатор
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(kafkaTopicProperties.hubs(), null, event.getTimestamp().toEpochMilli(), null, eventAvro);

        // отправляем данные
        producer.send(record);
    }

    private HubEventAvro getHubEventAvro(HubEvent event) {
        Object payload = null;
        if (event.getType() == HubEventType.DEVICE_ADDED) {
            payload = getDeviceAddedEventAvro(event);
        } else if (event.getType() == HubEventType.DEVICE_REMOVED) {
            payload = getDeviceRemovedEventAvro(event);
        } else if (event.getType() == HubEventType.SCENARIO_ADDED) {
            payload = getScenarioAddedEventAvro(event);
        } else if (event.getType() == HubEventType.SCENARIO_REMOVED) {
            payload = getScenarioRemovedEventAvro(event);
        }

        HubEventAvro hubEventAvro = new HubEventAvro();
        hubEventAvro.setHubId(event.getHubId());
        hubEventAvro.setTimestamp(event.getTimestamp());
        hubEventAvro.setPayload(payload);

        return hubEventAvro;
    }

    private DeviceAddedEventAvro getDeviceAddedEventAvro(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;

        return new DeviceAddedEventAvro(deviceAddedEvent.getId(), getDeviceTypeAvro(deviceAddedEvent.getDeviceType()));
    }

    private DeviceRemovedEventAvro getDeviceRemovedEventAvro(HubEvent event) {
        return new DeviceRemovedEventAvro(((DeviceRemovedEvent) event).getId());
    }

    private ScenarioAddedEventAvro getScenarioAddedEventAvro(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;

        ScenarioAddedEventAvro scenarioAddedEventAvro = new ScenarioAddedEventAvro();
        scenarioAddedEventAvro.setName(scenarioAddedEvent.getName());

        List<ScenarioConditionAvro> conditions = scenarioAddedEvent.getConditions().stream()
                .map(this::getScenarioConditionAvro)
                .toList();
        scenarioAddedEventAvro.setConditions(conditions);

        List<DeviceActionAvro> actions = scenarioAddedEvent.getActions().stream()
                .map(this::getDeviceActionAvro)
                .toList();
        scenarioAddedEventAvro.setActions(actions);

        return scenarioAddedEventAvro;
    }

    private ScenarioRemovedEventAvro getScenarioRemovedEventAvro(HubEvent event) {
        return new ScenarioRemovedEventAvro(((ScenarioRemovedEvent) event).getName());
    }

    private DeviceTypeAvro getDeviceTypeAvro(DeviceType deviceType) {
        if (deviceType == DeviceType.MOTION_SENSOR) return DeviceTypeAvro.MOTION_SENSOR;
        else if (deviceType == DeviceType.TEMPERATURE_SENSOR) return DeviceTypeAvro.TEMPERATURE_SENSOR;
        else if (deviceType == DeviceType.LIGHT_SENSOR) return DeviceTypeAvro.LIGHT_SENSOR;
        else if (deviceType == DeviceType.CLIMATE_SENSOR) return DeviceTypeAvro.CLIMATE_SENSOR;
        else if (deviceType == DeviceType.SWITCH_SENSOR) return DeviceTypeAvro.SWITCH_SENSOR;

        throw new RuntimeException("Нет соответствия значения перечисления DeviceType " + deviceType.toString() + " в перечислении DeviceTypeAvro.");
    }

    private ScenarioConditionAvro getScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        ScenarioConditionAvro scenarioConditionAvro = new ScenarioConditionAvro();
        scenarioConditionAvro.setSensorId(scenarioCondition.getSensorId());
        scenarioConditionAvro.setType(getConditionTypeAvro(scenarioCondition.getType()));
        scenarioConditionAvro.setOperation(getConditionOperationAvro(scenarioCondition.getOperation()));
        scenarioConditionAvro.setValue(scenarioCondition.getValue());

        return scenarioConditionAvro;
    }

    private DeviceActionAvro getDeviceActionAvro(DeviceAction deviceAction) {
        DeviceActionAvro deviceActionAvro = new DeviceActionAvro();
        deviceActionAvro.setSensorId(deviceAction.getSensorId());
        deviceActionAvro.setType(getActionTypeAvro(deviceAction.getType()));
        deviceActionAvro.setValue(deviceAction.getValue());

        return deviceActionAvro;
    }

    private ConditionOperationAvro getConditionOperationAvro(ConditionOperation conditionOperation) {
        if (conditionOperation == ConditionOperation.EQUALS) return ConditionOperationAvro.EQUALS;
        else if (conditionOperation == ConditionOperation.GREATER_THAN) return ConditionOperationAvro.GREATER_THAN;
        else if (conditionOperation == ConditionOperation.LOWER_THAN) return ConditionOperationAvro.LOWER_THAN;

        throw new RuntimeException("Нет соответствия значения перечисления ConditionOperation " + conditionOperation.toString() + " в перечислении ConditionOperationAvro.");
    }

    private ConditionTypeAvro getConditionTypeAvro(ConditionType conditionType) {
        if (conditionType == ConditionType.MOTION) return ConditionTypeAvro.MOTION;
        else if (conditionType == ConditionType.LUMINOSITY) return ConditionTypeAvro.LUMINOSITY;
        else if (conditionType == ConditionType.SWITCH) return ConditionTypeAvro.SWITCH;
        else if (conditionType == ConditionType.TEMPERATURE) return ConditionTypeAvro.TEMPERATURE;
        else if (conditionType == ConditionType.CO2LEVEL) return ConditionTypeAvro.CO2LEVEL;
        else if (conditionType == ConditionType.HUMIDITY) return ConditionTypeAvro.HUMIDITY;

        throw new RuntimeException("Нет соответствия значения перечисления ConditionType " + conditionType.toString() + " в перечислении ConditionTypeAvro.");
    }

    private ActionTypeAvro getActionTypeAvro(ActionType actionType) {
        if (actionType == ActionType.ACTIVATE) return ActionTypeAvro.ACTIVATE;
        else if (actionType == ActionType.DEACTIVATE) return ActionTypeAvro.DEACTIVATE;
        else if (actionType == ActionType.INVERSE) return ActionTypeAvro.INVERSE;
        else if (actionType == ActionType.SET_VALUE) return ActionTypeAvro.SET_VALUE;

        throw new RuntimeException("Нет соответствия значения перечисления ActionType " + actionType.toString() + " в перечислении ActionTypeAvro.");
    }
}

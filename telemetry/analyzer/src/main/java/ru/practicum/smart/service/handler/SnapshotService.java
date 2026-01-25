package ru.practicum.smart.service.handler;

import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.smart.grpc.HubRouterGrpcClient;
import ru.practicum.smart.model.Action;
import ru.practicum.smart.model.Condition;
import ru.practicum.smart.model.Scenario;
import ru.practicum.smart.storage.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class SnapshotService {
    private static final Logger log = LoggerFactory.getLogger(SnapshotService.class);

    private final ScenarioRepository scenarioRepository;
    private final HubRouterGrpcClient hubRouterGrpcClient;

    @Autowired
    public SnapshotService(ScenarioRepository scenarioRepository,
                           HubRouterGrpcClient hubRouterGrpcClient) {
        this.scenarioRepository = scenarioRepository;
        this.hubRouterGrpcClient = hubRouterGrpcClient;
    }

    public void handleRecord(SensorsSnapshotAvro sensorsSnapshotAvro) {
        String hubId = sensorsSnapshotAvro.getHubId();

        log.info("Получен снапшот от хаба с hubId = {}.", hubId);

        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        if (scenarios.isEmpty()) {
            log.warn("Для хаба с hubId = {} нет сценариев.", hubId);

            return;
        }

        for (Scenario scenario : scenarios) {
            if (analyzeScenario(scenario, sensorsSnapshotAvro)) {
                log.info("Условия сценария '{}' для хаба {} выполнены.", scenario.getName(), sensorsSnapshotAvro.getHubId());

                executeActions(scenario, sensorsSnapshotAvro);
            }
        }
    }

    private boolean analyzeScenario(Scenario scenario, SensorsSnapshotAvro snapshot) {
        Map<String, Condition> conditions = scenario.getConditions();
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();

        // Перебираем все условия сценария.
        for (Map.Entry<String, Condition> entry : conditions.entrySet()) {
            String sensorId = entry.getKey();
            Condition condition = entry.getValue();

            SensorStateAvro sensorState = sensorsState.get(sensorId);
            if (sensorState == null) return false;

            Integer sensorValue = getValueFromSensorState(sensorState, condition.getType());
            if (sensorValue == null) return false;

            if (!condition.check(sensorValue)) return false;
        }
        return true;
    }

    private Integer getValueFromSensorState(SensorStateAvro sensorState, ConditionTypeAvro conditionType) {
        Object data = sensorState.getData();

        return switch (conditionType) {
            case TEMPERATURE -> switch (data) {
                case TemperatureSensorAvro temp -> temp.getTemperatureC();
                case ClimateSensorAvro climate -> climate.getTemperatureC();
                default -> null;
            };
            case HUMIDITY -> (data instanceof ClimateSensorAvro climate) ? climate.getHumidity() : null;
            case CO2LEVEL -> (data instanceof ClimateSensorAvro climate) ? climate.getCo2Level() : null;
            case LUMINOSITY -> (data instanceof LightSensorAvro light) ? light.getLuminosity() : null;
            case MOTION -> (data instanceof MotionSensorAvro motion) ? (motion.getMotion() ? 1 : 0) : null;
            case SWITCH -> (data instanceof SwitchSensorAvro sw) ? (sw.getState() ? 1 : 0) : null;
        };
    }

    private void executeActions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        Map<String, Action> actions = scenario.getActions();

        for (Map.Entry<String, Action> entry : actions.entrySet()) {
            String sensorId = entry.getKey();
            Action action = entry.getValue();

            DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(ActionTypeProto.valueOf(action.getType().toString()));

            if (action.getValue() != null) actionBuilder.setValue(action.getValue());

            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(snapshot.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(actionBuilder.build())
                    .setTimestamp(Timestamp.newBuilder()
                            .setSeconds(Instant.now().getEpochSecond())
                            .setNanos(Instant.now().getNano())
                            .build())
                    .build();

            hubRouterGrpcClient.sendDeviceAction(request);

            log.info("Отправлено действие по сценарию '{}' для сенсора {}", scenario.getName(), sensorId);
        }
    }
}

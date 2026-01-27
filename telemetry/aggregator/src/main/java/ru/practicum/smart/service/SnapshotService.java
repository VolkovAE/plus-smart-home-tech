package ru.practicum.smart.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SnapshotService {
    private final Map<String, SensorsSnapshotAvro> sensorsSnapshots = new HashMap<>();  // снапшоты в разрезе хабов

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro curSnapshot = sensorsSnapshots.computeIfAbsent(event.getHubId(), hubId ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );

        SensorStateAvro oldStateSensor = curSnapshot.getSensorsState().get(event.getId());
        if (oldStateSensor != null)
            if ((oldStateSensor.getTimestamp().isAfter(event.getTimestamp())) || (oldStateSensor.getData().equals(event.getPayload())))
                return Optional.empty();

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        curSnapshot.setTimestamp(event.getTimestamp());
        curSnapshot.getSensorsState().put(event.getId(), newState);

        return Optional.of(curSnapshot);
    }
}

package ru.practicum.smart.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.smart.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);
}

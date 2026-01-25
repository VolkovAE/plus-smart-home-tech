package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

import static ru.practicum.smart.util.StringConstants.TABLE_NAME_ENTITY_CONDITION;

@Entity
@Table(name = TABLE_NAME_ENTITY_CONDITION)
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    private ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    private ConditionOperationAvro operation;

    private Integer value;

    public boolean check(Integer sensorValue) {
        if (sensorValue == null || value == null) return false;
        return switch (operation) {
            case EQUALS -> sensorValue.equals(value);
            case GREATER_THAN -> sensorValue > value;
            case LOWER_THAN -> sensorValue < value;
        };
    }
}

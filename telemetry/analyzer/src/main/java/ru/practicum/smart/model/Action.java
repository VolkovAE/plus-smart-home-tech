package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

import static ru.practicum.smart.util.StringConstants.TABLE_NAME_ENTITY_ACTION;

@Entity
@Table(name = TABLE_NAME_ENTITY_ACTION)
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Enumerated(EnumType.STRING)
    private ActionTypeAvro type;

    private Integer value;
}

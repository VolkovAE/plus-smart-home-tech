package ru.practicum.smart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static ru.practicum.smart.util.StringConstants.TABLE_NAME_ENTITY_SENSOR;

@Entity
@Table(name = TABLE_NAME_ENTITY_SENSOR)
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Column(name = "hub_id")
    private String hubId;
}

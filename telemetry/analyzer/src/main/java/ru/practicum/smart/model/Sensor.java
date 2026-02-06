package ru.practicum.smart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static ru.practicum.smart.util.StringConstants.TABLE_NAME_ENTITY_SENSOR;

@Entity
@Table(name = TABLE_NAME_ENTITY_SENSOR)
@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sensor {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    String id;

    @Column(name = "hub_id", nullable = false)
    private String hubId;
}

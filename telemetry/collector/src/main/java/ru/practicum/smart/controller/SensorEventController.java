package ru.practicum.smart.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.smart.model.sensor.SensorEvent;
import ru.practicum.smart.service.sensor.SensorService;

import static ru.practicum.smart.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_EVENTS;
import static ru.practicum.smart.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_SENSORS;

@Validated
@RestController
@RequestMapping(path = REQUEST_MAPPING_PATH_EVENTS)
public class SensorEventController {
    private final SensorService sensorService;

    private static final Logger log = LoggerFactory.getLogger(SensorEventController.class);

    @Autowired
    public SensorEventController(@Qualifier("SensorServiceImpl") SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping(REQUEST_MAPPING_PATH_SENSORS)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        sensorService.toCollect(event);
    }
}

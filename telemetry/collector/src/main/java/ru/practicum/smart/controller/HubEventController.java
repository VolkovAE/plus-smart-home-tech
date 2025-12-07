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
import ru.practicum.smart.model.hub.HubEvent;
import ru.practicum.smart.service.hub.HubService;

import static ru.practicum.smart.util.StringConstants.REQUEST_MAPPING_PATH_EVENTS;
import static ru.practicum.smart.util.StringConstants.REQUEST_MAPPING_PATH_HUBS;

@Validated
@RestController
@RequestMapping(path = REQUEST_MAPPING_PATH_EVENTS)
public class HubEventController {
    private final HubService hubService;

    private static final Logger log = LoggerFactory.getLogger(HubEventController.class);

    @Autowired
    public HubEventController(@Qualifier("HubServiceImpl") HubService hubService) {
        this.hubService = hubService;
    }

    @PostMapping(REQUEST_MAPPING_PATH_HUBS)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        hubService.toCollect(event);
    }
}

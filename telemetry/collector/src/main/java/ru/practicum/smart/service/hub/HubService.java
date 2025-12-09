package ru.practicum.smart.service.hub;

import ru.practicum.smart.model.hub.HubEvent;

public interface HubService {
    void toCollect(HubEvent event);
}

package ru.practicum.smart.service.hub;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.smart.model.hub.HubEvent;

@Service
@Qualifier("HubServiceImpl")
public class HubServiceImpl implements HubService {
    @Override
    public void toCollect(HubEvent event) {
        // реализовать кодирование и отправку сообщений хаба в части датчиков в кафку
    }
}

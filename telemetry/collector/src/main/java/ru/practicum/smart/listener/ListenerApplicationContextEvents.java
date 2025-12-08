package ru.practicum.smart.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_PRODUCER_KAFKA_TELEMETRY;

@Slf4j
@Component
public class ListenerApplicationContextEvents {
    private final Producer<String, SpecificRecordBase> producer;

    @Autowired
    public ListenerApplicationContextEvents(@Qualifier(BEAN_NAME_PRODUCER_KAFKA_TELEMETRY) Producer<String, SpecificRecordBase> producer) {
        this.producer = producer;
    }

    @EventListener(classes = {ContextClosedEvent.class})
    public void handleContextClosedEvent() {
        closeProducerKafka();
    }

    private void closeProducerKafka() {
        producer.flush();
        producer.close();

        log.info("Продюсер Kafka-collector закрыт.");
    }
}

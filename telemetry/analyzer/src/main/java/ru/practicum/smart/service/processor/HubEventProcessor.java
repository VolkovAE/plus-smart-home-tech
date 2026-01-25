package ru.practicum.smart.service.processor;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.service.handler.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_CONSUMER_HUB_KAFKA_ANALYZER;

@Component
public class HubEventProcessor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HubEventProcessor.class);

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final Consumer<Void, HubEventAvro> consumer;

    private final HubEventService hubEventService;

    @Autowired
    public HubEventProcessor(@Qualifier(BEAN_NAME_CONSUMER_HUB_KAFKA_ANALYZER) Consumer<Void, HubEventAvro> consumer,
                             HubEventService hubEventService) {
        this.consumer = consumer;
        this.hubEventService = hubEventService;
    }

    @Override
    public void run() {
        try {
            // Цикл обработки событий
            while (true) {
                ConsumerRecords<Void, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<Void, HubEventAvro> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record);
                    // фиксируем оффсеты обработанных записей, если нужно
                    manageOffsets(record, count, consumer);
                    count++;
                }
                // фиксируем максимальный оффсет обработанных записей
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<Void, HubEventAvro> record) throws InterruptedException {
        HubEventAvro eventAvro = record.value();

        hubEventService.handleRecord(eventAvro);
    }

    private static void manageOffsets(ConsumerRecord<Void, HubEventAvro> record, int count, Consumer<Void, HubEventAvro> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}

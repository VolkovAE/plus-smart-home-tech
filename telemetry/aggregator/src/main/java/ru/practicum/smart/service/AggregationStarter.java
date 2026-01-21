package ru.practicum.smart.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.smart.config.KafkaTopicSnapshots;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.practicum.smart.unil.StringConstants.BEAN_NAME_CONSUMER_KAFKA_AGGREGATOR;
import static ru.practicum.smart.unil.StringConstants.BEAN_NAME_PRODUCER_KAFKA_AGGREGATOR;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Component
public class AggregationStarter {
    private static final Logger log = LoggerFactory.getLogger(AggregationStarter.class);

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final Consumer<Void, SensorEventAvro> consumer;
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaTopicSnapshots kafkaTopicSnapshots;
    private final SnapshotService snapshotService;

    @Autowired
    public AggregationStarter(@Qualifier(BEAN_NAME_CONSUMER_KAFKA_AGGREGATOR) Consumer<Void, SensorEventAvro> consumer,
                              @Qualifier(BEAN_NAME_PRODUCER_KAFKA_AGGREGATOR) Producer<String, SpecificRecordBase> producer,
                              KafkaTopicSnapshots kafkaTopicSnapshots,
                              SnapshotService snapshotService) {
        this.consumer = consumer;
        this.producer = producer;
        this.kafkaTopicSnapshots = kafkaTopicSnapshots;
        this.snapshotService = snapshotService;
    }

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            // Цикл обработки событий
            while (true) {
                ConsumerRecords<Void, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<Void, SensorEventAvro> record : records) {
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
                producer.flush();
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<Void, SensorEventAvro> record) throws InterruptedException {
        SensorEventAvro eventAvro = record.value();

        Optional<SensorsSnapshotAvro> snapshotAvro = snapshotService.updateState(eventAvro);
        if (snapshotAvro.isEmpty()) return;

        SensorsSnapshotAvro snapshot = snapshotAvro.get();
        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                kafkaTopicSnapshots.snapshots(),
                null,
                snapshot.getTimestamp().toEpochMilli(),
                null,
                snapshot
        );

        producer.send(producerRecord);
    }

    private static void manageOffsets(ConsumerRecord<Void, SensorEventAvro> record, int count, Consumer<Void, SensorEventAvro> consumer) {
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

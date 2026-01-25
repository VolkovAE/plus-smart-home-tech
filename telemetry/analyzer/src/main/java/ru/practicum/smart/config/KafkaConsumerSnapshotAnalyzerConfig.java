package ru.practicum.smart.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import ru.practicum.smart.deserializer.SensorSnapshotDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Properties;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_CONSUMER_SNAPSHOT_KAFKA_ANALYZER;

@Configuration
public class KafkaConsumerSnapshotAnalyzerConfig {
    private final KafkaConsumerSnapshotProperties kafkaConsumerSnapshotProperties;

    @Autowired
    public KafkaConsumerSnapshotAnalyzerConfig(KafkaConsumerSnapshotProperties kafkaConsumerSnapshotProperties) {
        this.kafkaConsumerSnapshotProperties = kafkaConsumerSnapshotProperties;
    }

    @Bean(name = BEAN_NAME_CONSUMER_SNAPSHOT_KAFKA_ANALYZER)
    @Description(value = "Единый потребитель сообщений из кафки по снапшотам (вход в аналайзер для снапшотов)")
    public Consumer<Void, SensorsSnapshotAvro> getConsumerHubAnalyzer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerSnapshotProperties.host() + ":" + kafkaConsumerSnapshotProperties.port());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorSnapshotDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerSnapshotProperties.group());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerSnapshotProperties.offset());

        Consumer<Void, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(kafkaConsumerSnapshotProperties.topic()));

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        return consumer;
    }
}

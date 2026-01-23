package ru.practicum.smart.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import ru.practicum.smart.deserializer.HubEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;
import java.util.Properties;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_CONSUMER_HUB_KAFKA_ANALYZER;

@Configuration
public class KafkaConsumerHubAnalyzerConfig {
    private final KafkaConsumerHubProperties kafkaConsumerHubProperties;

    @Autowired
    public KafkaConsumerHubAnalyzerConfig(KafkaConsumerHubProperties kafkaConsumerHubProperties) {
        this.kafkaConsumerHubProperties = kafkaConsumerHubProperties;
    }

    @Bean(name = BEAN_NAME_CONSUMER_HUB_KAFKA_ANALYZER)
    @Description(value = "Единый потребитель сообщений из кафки от различных хабов (вход в аналайзер для хабов)")
    public Consumer<Void, HubEventAvro> getConsumerHubAnalyzer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerHubProperties.host() + ":" + kafkaConsumerHubProperties.port());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerHubProperties.group());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerHubProperties.offset());

        Consumer<Void, HubEventAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(kafkaConsumerHubProperties.topic()));

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        return consumer;
    }
}

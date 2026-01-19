package ru.practicum.smart.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import ru.practicum.smart.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.List;
import java.util.Properties;

import static ru.practicum.smart.unil.StringConstants.BEAN_NAME_CONSUMER_KAFKA_TELEMETRY;

@Configuration
public class KafkaConsumerAggregatorConfig {
    private final KafkaConsumerProperties kafkaConsumerProperties;

    @Autowired
    public KafkaConsumerAggregatorConfig(KafkaConsumerProperties kafkaConsumerProperties) {
        this.kafkaConsumerProperties = kafkaConsumerProperties;
    }

    @Bean(name = BEAN_NAME_CONSUMER_KAFKA_TELEMETRY)
    @Description(value = "Единый потребитель сообщений из кафки от различных датчиков и хабов (вход в агрегатор)")
    public Consumer<Void, SensorEventAvro> getConsumerTelemetry() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.host() + ":" + kafkaConsumerProperties.port());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.group());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.offset());

        Consumer<Void, SensorEventAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(kafkaConsumerProperties.topic()));

        return consumer;
    }
}

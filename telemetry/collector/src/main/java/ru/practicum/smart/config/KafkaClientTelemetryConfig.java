package ru.practicum.smart.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import ru.practicum.smart.serializer.GeneralAvroSerializer;

import java.util.Properties;

import static ru.practicum.smart.util.StringConstants.BEAN_NAME_PRODUCER_KAFKA_TELEMETRY;
import static ru.practicum.smart.util.StringConstants.BOOTSTRAP_SERVERS_CONFIG;

@Configuration
public class KafkaClientTelemetryConfig {
    @Bean(name = BEAN_NAME_PRODUCER_KAFKA_TELEMETRY)
    @Description(value = "Единый поставщик сообщений от различных датчиков и хабов в кафку")
    public Producer<String, SpecificRecordBase> getProducerTelemetry() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        // указываем в качестве сериализатора ключа сообщения — VoidSerializer из комплекта kafka-clients
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        // указываем в качестве сериализатора данных сообщения наш Avro-сериализатор
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        return new KafkaProducer<>(config);
    }
}

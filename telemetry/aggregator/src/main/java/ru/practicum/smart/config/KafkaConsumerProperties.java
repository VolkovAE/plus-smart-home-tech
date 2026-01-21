package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aggregator.kafka.consumer", ignoreUnknownFields = true)
public record KafkaConsumerProperties(String host, String port, String offset, String group, String topic) {
}

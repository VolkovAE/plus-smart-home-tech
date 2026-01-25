package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analyzer.kafka.consumer-hub", ignoreUnknownFields = true)
public record KafkaConsumerHubProperties(String host, String port, String offset, String group, String topic) {
}

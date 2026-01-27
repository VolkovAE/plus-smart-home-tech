package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analyzer.kafka.consumer-snapshot", ignoreUnknownFields = true)
public record KafkaConsumerSnapshotProperties(String host, String port, String offset, String group, String topic) {
}

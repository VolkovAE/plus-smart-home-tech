package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aggregator.kafka.topic", ignoreUnknownFields = true)
public record KafkaTopicSnapshots(String snapshots) {
}

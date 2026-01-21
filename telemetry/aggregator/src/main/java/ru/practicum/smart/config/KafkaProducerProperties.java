package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aggregator.kafka.producer", ignoreUnknownFields = true)
public record KafkaProducerProperties(String host, String port) {
}

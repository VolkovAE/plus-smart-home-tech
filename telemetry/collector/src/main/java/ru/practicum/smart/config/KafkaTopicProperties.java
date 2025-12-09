package ru.practicum.smart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topic", ignoreUnknownFields = true)
public record KafkaTopicProperties(String sensors, String hubs) {
}

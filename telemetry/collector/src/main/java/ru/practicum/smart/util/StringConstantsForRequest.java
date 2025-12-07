package ru.practicum.smart.util;

public final class StringConstantsForRequest {
    private StringConstantsForRequest() {
    }

    public static final String REQUEST_MAPPING_PATH_EVENTS = "/events";
    public static final String REQUEST_MAPPING_PATH_SENSORS = "/sensors";
    public static final String REQUEST_MAPPING_PATH_HUBS = "/hubs";
    public static final String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";
    public static final String TOPIC_TELEMETRY = "telemetry.sensors.v1";
    public static final String BEAN_NAME_PRODUCER_KAFKA_TELEMETRY = "ProducerKafkaTelemetry";
}

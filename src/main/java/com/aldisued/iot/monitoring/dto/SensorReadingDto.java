package com.aldisued.iot.monitoring.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record SensorReadingDto(
    @NotNull(message = "Sensor ID must not be null") UUID sensorId,
    @NotNull(message = "Sensor reading value must not be null") Double value,
    @NotNull(message = "Sensor reading timestamp must not be null") LocalDateTime timestamp
) {}

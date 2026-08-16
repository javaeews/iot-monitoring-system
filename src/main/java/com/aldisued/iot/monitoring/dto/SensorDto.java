package com.aldisued.iot.monitoring.dto;

import com.aldisued.iot.monitoring.entity.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorDto(@NotBlank(message = "Sensor name must not be blank") String name,
		@NotNull(message = "Sensor type must not be null") SensorType type) {
}

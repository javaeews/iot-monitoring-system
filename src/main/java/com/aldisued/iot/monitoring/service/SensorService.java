package com.aldisued.iot.monitoring.service;

import org.springframework.stereotype.Service;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.ConflictException;
import com.aldisued.iot.monitoring.repository.SensorRepository;

/**
 * Service class for managing sensors.
 *
 */
@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Saves a new sensor to the database.
     *
     * @param sensor
     *            the sensor DTO containing the sensor details
     * @return the saved sensor entity
     * @throws IllegalArgumentException
     *             if the sensor DTO is null, if the sensor name is null or blank, or if the sensor type is null
     * @throws ConflictException
     *             if a sensor with the same name already exists
     *
     */
    public Sensor saveSensor(SensorDto sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor DTO must not be null");
        }
        if (sensor.name() == null || sensor.name().isBlank()) {
            throw new IllegalArgumentException("Sensor name must not be blank");
        }
        if (sensor.type() == null) {
            throw new IllegalArgumentException("Sensor type must not be null");
        }
        if (sensorRepository.existsByName(sensor.name())) {
            throw new ConflictException("Sensor with name '%s' already exists".formatted(sensor.name()));
        }

        return sensorRepository.saveAndFlush(new Sensor(sensor.name(), sensor.type()));
    }
}

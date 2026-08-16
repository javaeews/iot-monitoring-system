package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

/**
 * Service class for managing sensor readings.
 *
 */
@Service
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository, SensorRepository sensorRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
    }

    /**
     * Saves a new sensor reading to the database.
     *
     * @param sensorReadingDto
     *            the sensor reading DTO containing the reading details
     * @return the saved sensor reading entity
     * @throws IllegalArgumentException
     *             if the sensor reading DTO is null or any required field is missing
     * @throws EntityNotFoundException
     *             if the sensor with the given ID does not exist
     */
    public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
        if (sensorReadingDto == null) {
            throw new IllegalArgumentException("Sensor reading DTO must not be null");
        }
        if (sensorReadingDto.sensorId() == null) {
            throw new IllegalArgumentException("Sensor ID must not be null");
        }
        if (sensorReadingDto.value() == null) {
            throw new IllegalArgumentException("Sensor reading value must not be null");
        }
        if (sensorReadingDto.timestamp() == null) {
            throw new IllegalArgumentException("Sensor reading timestamp must not be null");
        }

        final Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
                .orElseThrow(() -> new EntityNotFoundException("Sensor with ID %s was not found".formatted(sensorReadingDto.sensorId())));

        final SensorReading sensorReading = new SensorReading(sensorReadingDto.value(), sensorReadingDto.timestamp(), sensor);
        return sensorReadingRepository.save(sensorReading);
    }

}

package com.aldisued.iot.monitoring.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;

/**
 * Service class for managing sensor measurements.
 *
 */
@Service
public class MeasurementService {

    private final SensorReadingRepository sensorReadingRepository;

    public MeasurementService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    /**
     * Retrieves the measurement values for a specific sensor type within a given time range.
     *
     * @param sensorType
     *            the type of sensor to filter by
     * @param from
     *            the start of the time range (inclusive)
     * @param to
     *            the end of the time range (inclusive)
     * @return a list of measurement values for the specified sensor type and time range
     * @throws IllegalArgumentException
     *             if the sensorType, from, or to parameters are null
     */
    public List<Double> getMeasurementValuesBySensorType(SensorType sensorType, LocalDateTime from, LocalDateTime to) {
        if (sensorType == null || from == null || to == null) {
            throw new IllegalArgumentException("Sensor type, from, and to must not be null");
        }
        return sensorReadingRepository.findBySensorTypeAndTimestampBetweenOrderByTimestampAsc(sensorType, from, to)
                .stream()
                .map(SensorReading::getValue)
                .toList();
    }

    /**
     * Calculates the average temperature from sensor readings within a specified time range.
     * 
     * @param from
     *            the start of the time range (inclusive)
     * @param to
     *            the end of the time range (inclusive)
     * @return an Optional containing the average temperature if readings are found, or an empty Optional if no readings are available
     * @throws IllegalArgumentException
     *             if the from or to parameters are null
     */
    public Optional<Double> getAverageTemperature(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and to must not be null");
        }
        return Optional.ofNullable(sensorReadingRepository.findAverageValueBySensorTypeAndTimestampBetween(SensorType.TEMPERATURE, from, to));
    }

}

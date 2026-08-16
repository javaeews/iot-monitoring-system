package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for managing alerts.
 *
 */
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;
    private final KafkaTemplate<String, AlertDto> kafkaTemplate;

    public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository, KafkaTemplate<String, AlertDto> kafkaTemplate) {
        this.alertRepository = alertRepository;
        this.sensorRepository = sensorRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Saves a new alert to the database and sends it to the Kafka topic.
     *
     * @param alertDto
     *            the alert DTO containing the alert details
     * @return the saved alert entity
     * @throws IllegalArgumentException
     *             if the alert DTO is null or if a required alert field is missing
     * @throws EntityNotFoundException
     *             if the sensor with the given ID does not exist
     */
    public Alert saveAlert(AlertDto alertDto) {
        if (alertDto == null) {
            throw new IllegalArgumentException("Alert DTO must not be null");
        }
        if (alertDto.sensorId() == null) {
            throw new IllegalArgumentException("Sensor ID must not be null");
        }
        if (alertDto.message() == null || alertDto.message().isBlank()) {
            throw new IllegalArgumentException("Alert message must not be blank");
        }
        if (alertDto.timestamp() == null) {
            throw new IllegalArgumentException("Alert timestamp must not be null");
        }
        Sensor sensor = sensorRepository.findById(alertDto.sensorId())
                .orElseThrow(() -> new EntityNotFoundException("Sensor with ID %s was not found".formatted(alertDto.sensorId())));

        Alert alert = alertRepository.save(new Alert(alertDto.message(), alertDto.timestamp(), sensor));

        kafkaTemplate.send("alerts", alertDto);
        return alert;
    }

    /**
     * Finds the last alert for a given sensor ID.
     *
     * @param sensorId
     *            the ID of the sensor
     * @return the last alert DTO for the given sensor ID
     * @throws IllegalArgumentException
     *             if the sensor ID is null
     * @throws EntityNotFoundException
     *             if no alert is found for the given sensor ID
     */
    public AlertDto findLastAlertBySensorId(UUID sensorId) {
        if (sensorId == null) {
            throw new IllegalArgumentException("Sensor ID must not be null");
        }
        Alert alert = alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId)
                .orElseThrow(() -> new EntityNotFoundException("Alert for sensor ID %s was not found".formatted(sensorId)));

        return new AlertDto(alert.getSensor().getId(), alert.getMessage(), alert.getTimestamp());
    }
}

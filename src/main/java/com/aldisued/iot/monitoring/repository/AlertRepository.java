package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.Alert;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing alerts.
 *
 */
public interface AlertRepository extends JpaRepository<Alert, Long> {

    Optional<Alert> findFirstBySensorIdOrderByTimestampDesc(UUID sensorId);
}

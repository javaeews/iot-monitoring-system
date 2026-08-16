package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing sensor readings.
 *
 */
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    @Query("""
            select sr
            from SensorReading sr
            where sr.sensor.type = :sensorType
              and sr.timestamp between :from and :to
            order by sr.timestamp asc
            """)
    List<SensorReading> findBySensorTypeAndTimestampBetweenOrderByTimestampAsc(@Param("sensorType") SensorType sensorType,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
            select avg(sr.value)
            from SensorReading sr
            where sr.sensor.type = :sensorType
              and sr.timestamp between :from and :to
            """)
    Double findAverageValueBySensorTypeAndTimestampBetween(@Param("sensorType") SensorType sensorType, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}

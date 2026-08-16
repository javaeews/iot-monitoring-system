package com.aldisued.iot.monitoring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Service class for calculating measurements and filtering data.
 *
 */
@Service
public class MeasurementCalculatorService {

    /**
     * Filters a list of Double values based on the average and a specified deviation.
     *
     * @param values
     *            the list of Double values to filter
     * @param deviation
     *            the allowed deviation from the average (between 0.0 and 1.0)
     * @return a list of Double values that are within the specified deviation from the average
     * @throws IllegalArgumentException
     *             if the deviation is not between 0.0 and 1.0, or if the values list is empty
     */
    public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
        if (deviation == null || deviation < 0.0 || deviation > 1.0) {
            throw new IllegalArgumentException("Deviation must be between 0.0 and 1.0");
        }

        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        double average = values.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow(() -> new IllegalArgumentException("Values must contain at least one non-null value"));
        double allowedDifference = Math.abs(average * deviation);
        double minAcceptedValue = average - allowedDifference;
        double maxAcceptedValue = average + allowedDifference;

        return values.stream().filter(value -> value >= minAcceptedValue && value <= maxAcceptedValue).toList();
    }

    /**
     * Calculates the moving average of a list of Double values using a specified window size.
     *
     * @param data
     *            the list of Double values to calculate the moving average for
     * @param windowSize
     *            the size of the window to use for calculating the moving average (must be greater than 0 and less than or equal to the size of the
     *            data list)
     * @return a list of Double values representing the moving averages
     * @throws IllegalArgumentException
     *             if the data list is empty, or if the window size is not valid
     */
    public List<Double> getMovingAverage(List<Double> data, int windowSize) {
        if (CollectionUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data must not be empty");
        }
        if (windowSize <= 0 || windowSize > data.size()) {
            throw new IllegalArgumentException("Window size must be between 1 and data size");
        }

        List<Double> movingAverages = new ArrayList<>();
        for (int i = 0; i <= data.size() - windowSize; i++) {
            double average = data.subList(i, i + windowSize).stream().mapToDouble(Double::doubleValue).average().orElseThrow();
            movingAverages.add(average);
        }
        return movingAverages;
    }

}

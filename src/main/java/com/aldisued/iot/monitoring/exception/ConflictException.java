package com.aldisued.iot.monitoring.exception;

/**
 * Indicates that a request cannot be completed because it conflicts with the current state of the resource.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}


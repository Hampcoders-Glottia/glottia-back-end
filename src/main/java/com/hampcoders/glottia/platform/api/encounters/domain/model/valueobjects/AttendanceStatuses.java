package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

/**
 * Enumeration of attendance statuses.
 * @summary
 * This enum defines the various attendance statuses for encounters.
 * Each status is associated with a unique integer value.
 * It can be used to standardize attendance status representation across the system.
 * @see IllegalArgumentException
 */
public enum AttendanceStatuses {
    /**
     * Reserved status.
     */
    RESERVED(1),
    /**
     * Checked-in status.
     */
    CHECKED_IN(2),
    /**
     * No-show status.
     */
    NO_SHOW(3),
    /**
     * Cancelled status.
     */
    CANCELLED(4);

    /**
     * Integer value associated with the attendance status.
     */
    private final int value;

    /**
     * Constructor for AttendanceStatuses enum.
     * @param value
     */
    AttendanceStatuses(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of the attendance status.
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Create an AttendanceStatuses from an integer value.
     * @param value
     * @return AttendanceStatuses
     */
    public static AttendanceStatuses fromValue(int value) {
        for (AttendanceStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AttendanceStatuses value: " + value);
    }
}
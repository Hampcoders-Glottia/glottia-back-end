package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

/**
 * Enumeration representing the various statuses of an encounter.
 * @summary
 * This enum defines the different states an encounter can be in during its lifecycle.
 * Each status is associated with a unique integer value.
 * It can be used to track and manage the progress of encounters within the system.
 * @see IllegalArgumentException
 */
public enum EncounterStatuses {
    /**
     * Draft status of the encounter.
     */
    DRAFT(1),
    /**
     * Published status of the encounter.
     */
    PUBLISHED(2),
    /**
     * Ready status of the encounter.
     */
    READY(3),
    /**
     * In progress status of the encounter.
     */
    IN_PROGRESS(4),
    /**
     * Completed status of the encounter.
     */
    COMPLETED(5),
    /**
     * Cancelled status of the encounter.
     */
    CANCELLED(6);

    /**
     * Integer value associated with the encounter status.
     */
    private final int value;

    /**
     * Constructor for EncounterStatuses enum.
     * @param value
     */
    EncounterStatuses(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of the encounter status.
     * @return int
     */
    public int getValue() {
        return value;
    }
 
    /**
     * Create an EncounterStatuses from an integer value.
     * @param value
     * @return EncounterStatuses
     */
    public static EncounterStatuses fromValue(int value) {
        for (EncounterStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid EncounterStatuses value: " + value);
    }
}

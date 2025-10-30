package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

/**
 * TableStatuses Value Object
 * Represents the different statuses a table can have in a venue
 */
public enum TableStatuses {
    AVAILABLE (1),
    RESERVED (2),
    UNAVAILABLE (3);

    private final int value;

    TableStatuses(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TableStatuses fromValue(int value) {
        for (TableStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TableStatuses value: " + value);
    }
}
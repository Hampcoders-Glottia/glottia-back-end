package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

/**
 * TableTypes Value Object
 * Represents the different types of tables available in the venues
 */
public enum TableTypes {
    ENCOUNTER_TABLE(1),
    GENERAL_TABLE(2);

    private final int value;

    TableTypes(int value) {
        this.value = value;
    }   

    public int getValue() {
        return value;
    }

    public static TableTypes fromValue(int value) {
        for (TableTypes type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TableTypes value: " + value);
    }
}

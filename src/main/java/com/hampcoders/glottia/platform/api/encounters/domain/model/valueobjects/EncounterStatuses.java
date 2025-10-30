package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

public enum EncounterStatuses {
    DRAFT(1),
    PUBLISHED(2), // Esperando Venue
    READY(3),     // Venue confirmado, mínimo de asistentes alcanzado
    IN_PROGRESS(4),
    COMPLETED(5),
    CANCELLED(6);

    private final int value;

    EncounterStatuses(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
 
    public static EncounterStatuses fromValue(int value) {
        for (EncounterStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid EncounterStatuses value: " + value);
    }
}

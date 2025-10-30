package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

public enum EncounterStatus {
    DRAFT,
    PUBLISHED, // Esperando Venue
    READY,     // Venue confirmado, mínimo de asistentes alcanzado
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

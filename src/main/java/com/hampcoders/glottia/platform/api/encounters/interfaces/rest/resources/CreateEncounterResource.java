package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateEncounterResource(
    Long creatorId, // Debería venir del token JWT, pero lo dejamos por si es admin
    Long venueId, 
    String topic, 
    String language,  // "ENGLISH", "SPANISH", etc.
    String cefrLevel, // "A1", "B2", etc.
    LocalDateTime scheduledAt
) {
    // Aquí se pueden agregar validaciones de @NotNull, @Future, etc.
}
package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

// Usado para POST /api/v1/encounters/{id}/attendances/check-in
public record CheckInEncounterResource(
    Long learnerId // En un sistema real, esto vendría del JWT/SecurityContext
    // String qrCode (Post-MVP)
) {}
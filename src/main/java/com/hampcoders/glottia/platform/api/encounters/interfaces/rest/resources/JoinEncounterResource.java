package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

// Usado para POST /api/v1/encounters/{id}/attendances
public record JoinEncounterResource(
    Long learnerId // En un sistema real, esto vendría del JWT/SecurityContext
) {}

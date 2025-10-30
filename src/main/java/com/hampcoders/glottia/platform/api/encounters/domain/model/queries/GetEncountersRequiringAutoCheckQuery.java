package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import java.time.LocalDateTime;

public record GetEncountersRequiringAutoCheckQuery(LocalDateTime thresholdTime) {} // Para auto-cancelación
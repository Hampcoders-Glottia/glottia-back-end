package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import java.time.LocalDateTime;

public record EncounterStartedEvent(
    Long encounterId, 
    LocalDateTime startedAt, 
    int checkedInCount) {}
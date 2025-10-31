package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import java.time.LocalDateTime;

public record EncounterPublishedEvent(
    Long encounterId, 
    String language, 
    String cefrLevel, 
    LocalDateTime scheduledAt, 
    int maxCapacity) {}
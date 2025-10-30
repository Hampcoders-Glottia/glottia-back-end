package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDateTime;

public record EncounterPublishedEvent(
    Long encounterId, 
    Language language, 
    CEFRLevel level, 
    LocalDateTime scheduledAt, 
    int maxCapacity) {}
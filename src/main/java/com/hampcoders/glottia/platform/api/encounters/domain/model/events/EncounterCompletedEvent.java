package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import java.time.LocalDateTime;
import java.util.List;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record EncounterCompletedEvent(
    Long encounterId,
     LocalDateTime completedAt, 
     int finalAttendes,
     List<LearnerId> attendeeIds) {}
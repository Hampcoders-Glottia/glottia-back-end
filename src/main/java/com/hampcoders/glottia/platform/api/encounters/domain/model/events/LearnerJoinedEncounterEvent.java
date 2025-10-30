package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record LearnerJoinedEncounterEvent(
    Long encounterId, 
    LearnerId learnerId, 
    int currentConfirmedCapacity, 
    int maxCapacity) {}

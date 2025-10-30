package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record EncounterCancelledEvent(
    Long encounterId, 
    TableId tableId,
    String reason, 
    LearnerId cancelledBy) {}
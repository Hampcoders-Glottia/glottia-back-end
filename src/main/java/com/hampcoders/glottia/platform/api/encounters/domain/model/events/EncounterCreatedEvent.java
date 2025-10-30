package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDateTime;

public record EncounterCreatedEvent(
    Long encounterId, 
    LearnerId creatorId, 
    VenueId venueId, 
    /* TableId tableId - se asigna después */ 
    LocalDateTime scheduledAt, 
    String topic) {}
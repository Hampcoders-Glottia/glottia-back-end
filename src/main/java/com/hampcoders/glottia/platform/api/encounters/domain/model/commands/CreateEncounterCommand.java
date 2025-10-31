package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDateTime;

public record CreateEncounterCommand(
    LearnerId creatorId, 
    VenueId venueId, 
    String topic, 
    String language, 
    String cefrLevel, 
    LocalDateTime scheduledAt) {
}
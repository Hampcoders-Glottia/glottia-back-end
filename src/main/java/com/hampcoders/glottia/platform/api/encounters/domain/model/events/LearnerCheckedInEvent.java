package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDateTime;

public record LearnerCheckedInEvent(
    Long attendanceId, 
    Long encounterId, 
    LearnerId learnerId,
    VenueId venueId, 
    LocalDateTime checkedInAt) {}

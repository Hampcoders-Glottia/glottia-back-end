package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record PointsAwardedEvent(
    LearnerId learnerId, 
    Integer pointsAdded,
    Integer totalPoints, 
    String reason, 
    Long relatedEncounterId, 
    Long relatedAttendanceId) {}
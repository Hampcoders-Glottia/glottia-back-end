package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record PointsPenalizedEvent(
    LearnerId learnerId, 
    Integer pointsDeducted, 
    Integer totalPoints, 
    String reason, 
    Long relatedEncounterId, 
    Long relatedAttendanceId) {}
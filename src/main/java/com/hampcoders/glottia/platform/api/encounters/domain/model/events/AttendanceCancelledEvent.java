package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record AttendanceCancelledEvent(
    Long attendanceId, 
    Long encounterId, 
    LearnerId learnerId, 
    boolean lateCancellation) {} // Indica si fue tardía

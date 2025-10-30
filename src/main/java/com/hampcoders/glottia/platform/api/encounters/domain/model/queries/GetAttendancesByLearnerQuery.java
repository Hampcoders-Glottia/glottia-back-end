package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record GetAttendancesByLearnerQuery(
    LearnerId learnerId, 
    AttendanceStatus status) {}

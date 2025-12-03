package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record GetPointsHistoryQuery(LearnerId learnerId, Integer page, Integer size) {}
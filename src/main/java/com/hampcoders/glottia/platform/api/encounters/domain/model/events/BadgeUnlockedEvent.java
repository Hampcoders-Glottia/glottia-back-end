package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record BadgeUnlockedEvent(LearnerId learnerId, String badgeType, Integer currentPoints) {}
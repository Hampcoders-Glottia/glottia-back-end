package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

public record LoyaltyAccountCreatedEvent(Long loyaltyAccountId, LearnerId learnerId) {}
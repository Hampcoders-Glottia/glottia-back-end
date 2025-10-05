package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;

public record GetPartnersBySubscriptionStatusQuery(
        SubscriptionStatus subscriptionStatus
) {
}
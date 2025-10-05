package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;

public record GetSubscriptionStatusByNameQuery(
        SubscriptionStatuses name
) {
}
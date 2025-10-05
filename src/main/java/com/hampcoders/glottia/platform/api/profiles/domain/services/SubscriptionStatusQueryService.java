package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllSubscriptionStatusesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetSubscriptionStatusByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetSubscriptionStatusByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for SubscriptionStatus entity operations
 */
public interface SubscriptionStatusQueryService {
    List<SubscriptionStatus> handle(GetAllSubscriptionStatusesQuery query);
    Optional<SubscriptionStatus> handle(GetSubscriptionStatusByIdQuery query);
    Optional<SubscriptionStatus> handle(GetSubscriptionStatusByNameQuery query);
}
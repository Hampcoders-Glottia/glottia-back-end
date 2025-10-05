package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllSubscriptionStatusesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetSubscriptionStatusByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetSubscriptionStatusByNameQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.SubscriptionStatusQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.SubscriptionStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of SubscriptionStatusQueryService
 * Handles subscription status query operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Service
public class SubscriptionStatusQueryServiceImpl implements SubscriptionStatusQueryService {

    private final SubscriptionStatusRepository subscriptionStatusRepository;

    public SubscriptionStatusQueryServiceImpl(SubscriptionStatusRepository subscriptionStatusRepository) {
        this.subscriptionStatusRepository = subscriptionStatusRepository;
    }

    @Override
    public List<SubscriptionStatus> handle(GetAllSubscriptionStatusesQuery query) {
        return this.subscriptionStatusRepository.findAll();
    }

    @Override
    public Optional<SubscriptionStatus> handle(GetSubscriptionStatusByIdQuery query) {
        return this.subscriptionStatusRepository.findById(query.subscriptionStatusId());
    }

    @Override
    public Optional<SubscriptionStatus> handle(GetSubscriptionStatusByNameQuery query) {
        return this.subscriptionStatusRepository.findByName(query.name());
    }
}
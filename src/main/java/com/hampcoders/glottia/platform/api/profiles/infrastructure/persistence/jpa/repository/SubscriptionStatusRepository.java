package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for SubscriptionStatus entity
 * Repository for Subscription Status entity operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Repository
public interface SubscriptionStatusRepository extends JpaRepository<SubscriptionStatus, Long> {
    
    /**
     * Find subscription status by name enum
     * @param name the subscription status enum to search for
     * @return Optional<SubscriptionStatus> containing the subscription status if found
     */
    Optional<SubscriptionStatus> findByName(SubscriptionStatuses name);
    
    /**
     * Check if subscription status exists by name enum
     * @param name the subscription status enum to check
     * @return true if subscription status exists, false otherwise
     */
    boolean existsByName(SubscriptionStatuses name);
}
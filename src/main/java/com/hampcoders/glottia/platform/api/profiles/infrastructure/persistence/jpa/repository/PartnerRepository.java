package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findBySubscriptionStatus(SubscriptionStatus subscriptionStatus);
    boolean existsByTaxId(String taxId);
    boolean existsByTaxIdAndIdIsNot(String taxId, Long id);
}
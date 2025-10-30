package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByTaxId(String taxId);
    Optional<Partner> findByBusinessNameIgnoreCase(String businessName);
    List<Partner> findBySubscriptionStatusId(Long subscriptionStatusId);
    boolean existsByTaxId(String taxId);
    boolean existsByTaxIdAndIdIsNot(String taxId, Long id);
}
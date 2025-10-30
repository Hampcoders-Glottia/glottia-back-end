package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByPartnerId(PartnerId partnerId);
    Optional<Promotion> findByIdAndIsActive(Long id, boolean isActive);
    List<Promotion> findByIsActive(boolean isActive);  // Para catálogo active
    boolean existsByPartnerId(PartnerId partnerId);  // Si necesitas checks
    boolean existsByNameAndIsActiveAndPromotionTypeName(String name, boolean isActive, String promotionType);
	boolean existsByNameAndPartnerId(String name, PartnerId partnerId);
}
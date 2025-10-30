package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionTypes;

@Repository
public interface PromotionTypeRepository extends JpaRepository<PromotionType, Long> {
    boolean existsByName(PromotionTypes name);
    Optional<PromotionType> findByName(PromotionTypes name);
}
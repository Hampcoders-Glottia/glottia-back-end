package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VenuePromotionRepository extends JpaRepository<VenuePromotion, Long> {
    List<VenuePromotion> findByVenueId(Long venueId);
    List<VenuePromotion> findByVenueIdAndIsActive(Long venueId, boolean isActive);  // Para active
    List<VenuePromotion> findByVenueIdAndValidUntilBefore(Long venueId, LocalDate date);  // Para expired
    Optional<VenuePromotion> findByIdAndVenueId(Long id, Long venueId);
	boolean existsByVenueIdAndPromotionIdAndIsActive(Long venueId, Long promotionId, boolean isActive);
    Optional<VenuePromotion> findByVenueIdAndPromotionId(Long venueId, Long promotionId);
}

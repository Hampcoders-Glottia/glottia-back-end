package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.*;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PromotionRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenuePromotionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionQueryServiceImpl implements PromotionQueryService {
    private final PromotionRepository promotionRepository;
    private final VenuePromotionRepository venuePromotionRepository;

    public PromotionQueryServiceImpl(PromotionRepository promotionRepository, VenuePromotionRepository venuePromotionRepository) {
        this.promotionRepository = promotionRepository;
        this.venuePromotionRepository = venuePromotionRepository;
    }

    @Override
    public List<Promotion> handle(GetAllPromotionsQuery query) {
        return promotionRepository.findAll(); 
    }

    @Override
    public List<Promotion> handle(GetPromotionCatalogQuery query) {
        return promotionRepository.findByIsActive(true); 
    }

    @Override
    public List<VenuePromotion> handle(GetAllPromotionsByVenueIdQuery query) {
        return venuePromotionRepository.findByVenueId(query.venueId());  
    }

    @Override
    public List<VenuePromotion> handle(GetActivePromotionsByVenueIdQuery query) {
        return venuePromotionRepository.findByVenueIdAndIsActive(query.venueId(), true); 
    }

    @Override
    public List<VenuePromotion> handle(GetExpiredPromotionsByVenueIdQuery query) {
        return venuePromotionRepository.findByVenueIdAndValidUntilBefore(query.venueId(), LocalDate.now()); 
    }

    @Override
    public Optional<Promotion> handle(GetPromotionByIdQuery query) {
        return promotionRepository.findById(query.promotionId());  
    }

    @Override
    public Optional<VenuePromotion> handle(GetVenuePromotionByVenueIdAndPromotionIdQuery query) {
        return venuePromotionRepository.findByVenueIdAndPromotionId(query.venueId(), query.promotionId());
    }

	@Override
	public Optional<VenuePromotion> handle(GetVenuePromotionByIdQuery query) {
        return venuePromotionRepository.findById(query.venuePromotionId());
	}
}
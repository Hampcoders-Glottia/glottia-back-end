package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllPromotionTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetPromotionTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PromotionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionTypeQueryServiceImpl implements PromotionTypeQueryService {
    private final PromotionTypeRepository promotionTypeRepository;

    public PromotionTypeQueryServiceImpl(PromotionTypeRepository promotionTypeRepository) {
        this.promotionTypeRepository = promotionTypeRepository;
    }

    @Override
    public List<PromotionType> handle(GetAllPromotionTypesQuery query) {
        return promotionTypeRepository.findAll();
    }

    @Override
    public Optional<PromotionType> handle(GetPromotionTypeByNameQuery query) {
        return promotionTypeRepository.findByName(query.name());
    }
}
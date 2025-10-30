package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllPromotionTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetPromotionTypeByNameQuery;

public interface PromotionTypeQueryService {
    List<PromotionType> handle(GetAllPromotionTypesQuery query);
    Optional<PromotionType> handle(GetPromotionTypeByNameQuery query);
}
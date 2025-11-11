package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetActivePromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetAllPromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetAllPromotionsQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetExpiredPromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetPromotionByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetPromotionCatalogQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetVenuePromotionByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetVenuePromotionByVenueIdAndPromotionIdQuery;


public interface PromotionQueryService {
    List<Promotion> handle(GetAllPromotionsQuery query);  // Devuelve Promotion entidades
    List<Promotion> handle(GetPromotionCatalogQuery query);  // Devuelve Promotion entidades
    List<VenuePromotion> handle(GetAllPromotionsByVenueIdQuery query);  // Devuelve VenuePromotion por venue
    List<VenuePromotion> handle(GetActivePromotionsByVenueIdQuery query);  // Devuelve VenuePromotion activas
    List<VenuePromotion> handle(GetExpiredPromotionsByVenueIdQuery query);  // Devuelve VenuePromotion expiradas
    Optional<Promotion> handle(GetPromotionByIdQuery query);  // Devuelve Promotion por ID
    Optional<VenuePromotion> handle(GetVenuePromotionByVenueIdAndPromotionIdQuery query);  // Devuelve VenuePromotion por venue y promotion ID
    Optional<VenuePromotion> handle(GetVenuePromotionByIdQuery query);  // Devuelve VenuePromotion por ID
}
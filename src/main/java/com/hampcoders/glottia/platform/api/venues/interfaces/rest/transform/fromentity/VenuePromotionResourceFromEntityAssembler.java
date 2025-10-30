package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.VenuePromotionResource;

public class VenuePromotionResourceFromEntityAssembler {

    public static VenuePromotionResource toResourceFromEntity(VenuePromotion entity) {
        return new VenuePromotionResource(
            entity.getId(),
            entity.getPromotion().getId(),
            entity.getVenue().getId(),
            entity.getValidFrom().toString(),
            entity.getValidUntil().toString(),
            entity.getMaxRedemptions(),
            entity.getCurrentRedemptions(),
            entity.getIsActive()
        );
    }
}

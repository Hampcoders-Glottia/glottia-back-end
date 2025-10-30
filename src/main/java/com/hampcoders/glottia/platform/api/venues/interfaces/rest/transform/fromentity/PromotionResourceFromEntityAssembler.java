package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.PromotionResource;

public class PromotionResourceFromEntityAssembler {

    public static PromotionResource toResourceFromEntity(Promotion entity) {
        return new PromotionResource(
            entity.getId(),
            entity.getPartnerId().partnerId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPromotionType().getStringName(),
            entity.getValue(),
            entity.getIsActive()
        );
    }
}

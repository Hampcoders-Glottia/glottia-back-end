package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.PromotionTypeResource;

public class PromotionTypeResourceFromEntityAssembler {

    public static PromotionTypeResource toResourceFromEntity(PromotionType entity) {
        return new PromotionTypeResource(
            entity.getId(),
            entity.getStringName()
        );

    }
}

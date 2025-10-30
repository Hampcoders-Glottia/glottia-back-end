package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.CreatePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.CreatePromotionResource;

public class CreatePromotionCommandFromResourceAssembler {

    public static CreatePromotionCommand toCommandFromResource(CreatePromotionResource resource) {
        return new CreatePromotionCommand(
                new PartnerId(resource.partnerId()),
                resource.name(),
                resource.description(),
                resource.promotionType(),
                resource.value()
        );
    } 
}

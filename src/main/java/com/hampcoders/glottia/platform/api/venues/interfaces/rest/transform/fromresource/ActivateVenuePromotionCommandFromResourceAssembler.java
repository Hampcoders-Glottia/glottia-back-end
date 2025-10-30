package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.ActivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.ActivateVenuePromotionResource;

public class ActivateVenuePromotionCommandFromResourceAssembler {
    public static ActivateVenuePromotionCommand toCommandFromResource(Long venueId, ActivateVenuePromotionResource resource) {
        return new ActivateVenuePromotionCommand(
            venueId,
            resource.venuePromotionId()
        );
    }
}

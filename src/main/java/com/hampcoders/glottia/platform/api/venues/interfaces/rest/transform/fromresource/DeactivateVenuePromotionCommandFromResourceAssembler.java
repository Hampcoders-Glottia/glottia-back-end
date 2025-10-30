package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.DeactivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.DeactivateVenuePromotionResource;

public class DeactivateVenuePromotionCommandFromResourceAssembler {
    public static DeactivateVenuePromotionCommand toCommandFromResource(Long venueId, DeactivateVenuePromotionResource resource) {
        return new DeactivateVenuePromotionCommand(
            venueId,
            resource.venuePromotionId(),
            resource.reason()
        );
    }
}

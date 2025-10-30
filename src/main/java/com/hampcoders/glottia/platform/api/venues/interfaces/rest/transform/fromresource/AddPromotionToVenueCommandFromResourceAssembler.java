package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.AddPromotionToVenueCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.AddPromotionToVenueResource;

public class AddPromotionToVenueCommandFromResourceAssembler {

    public static AddPromotionToVenueCommand toCommandFromResource(Long venueId,AddPromotionToVenueResource resource) {
        return new AddPromotionToVenueCommand(
                venueId,
                resource.promotionId(),
                resource.validFrom(),
                resource.validUntil(),
                resource.maxRedemptions()
        );

    }
}

package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.VenueResource;

public class VenueResourceFromEntityAssembler {
    public static VenueResource toResourceFromEntity(Venue entity) {
        var venuePromotionResource = entity.getPromotionList().getPromotionItems().stream().map(VenuePromotionResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new VenueResource(
            entity.getId(),
            entity.getName(),
            entity.getAddress().street(),
            entity.getAddress().city(),
            entity.getAddress().state(),
            entity.getAddress().postalCode(),
            entity.getAddress().country(),
            entity.getVenueType().getStringTypeName(),
            entity.isActive(),
            venuePromotionResource
        );
    }

}

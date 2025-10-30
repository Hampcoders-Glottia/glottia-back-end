package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.PartnerVenueRegistryResource;

public class PartnerVenueRegistryResourceFromEntityAssembler {
    public static PartnerVenueRegistryResource toResourceFromEntity(PartnerVenueRegistry entity) {
        var venueRegistrationResource = entity.getVenueList().getRegistrations().stream().map(VenueRegistrationResourceFromEntityAssembler::toResourceFromEntity).toList(); 
        return new PartnerVenueRegistryResource(
            entity.getId(),
            entity.getPartnerId().partnerId(),
            venueRegistrationResource
        );
    }
}

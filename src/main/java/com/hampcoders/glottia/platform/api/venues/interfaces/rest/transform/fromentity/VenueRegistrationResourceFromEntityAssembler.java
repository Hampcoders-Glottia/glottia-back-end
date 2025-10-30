package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.VenueRegistrationResource;

public class VenueRegistrationResourceFromEntityAssembler {

    public static VenueRegistrationResource toResourceFromEntity(VenueRegistration entity) {
        return new VenueRegistrationResource(
            entity.getId(),
            entity.getPartnerVenueRegistry().getId(),
            entity.getVenue().getId(),
            entity.getRegistrationDate().toString(),
            entity.getIsActive()
        );
    }
}

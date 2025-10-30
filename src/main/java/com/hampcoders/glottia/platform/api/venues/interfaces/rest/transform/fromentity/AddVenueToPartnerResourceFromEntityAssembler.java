package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.AddVenueToPartnerRegistryResource;

public class AddVenueToPartnerResourceFromEntityAssembler {
    public static AddVenueToPartnerRegistryResource toResource(Long partnerId, VenueRegistration entity) {
        return new AddVenueToPartnerRegistryResource(partnerId, entity.getRegistrationDate(), entity.getIsActive());
    }
}

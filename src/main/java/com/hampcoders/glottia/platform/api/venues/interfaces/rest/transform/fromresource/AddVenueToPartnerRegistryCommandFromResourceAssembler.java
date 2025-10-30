package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.AddVenueToPartnerRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.AddVenueToPartnerRegistryResource;

public class AddVenueToPartnerRegistryCommandFromResourceAssembler {
    public static AddVenueToPartnerRegistryCommand toCommandFromResource(Long partnerId, AddVenueToPartnerRegistryResource resource){
        return new AddVenueToPartnerRegistryCommand(
                new PartnerId(partnerId),
                resource.venueId(),
                resource.registrationDate(),
                resource.isActive()
        );
    }
}

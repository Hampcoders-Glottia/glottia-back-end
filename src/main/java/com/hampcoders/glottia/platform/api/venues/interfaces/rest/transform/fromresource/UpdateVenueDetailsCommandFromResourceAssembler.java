package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.UpdateVenueDetailsCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.UpdateVenueDetailsResource;

public class UpdateVenueDetailsCommandFromResourceAssembler {
    public static UpdateVenueDetailsCommand toCommandFromResource(Long venueId,UpdateVenueDetailsResource resource) {
        var address = new Address(
            resource.street(),
            resource.city(),
            resource.state(),
            resource.postalCode(),
            resource.country()
        );
        return new UpdateVenueDetailsCommand(
            venueId,
            resource.name(),
            address,
            resource.venueTypeId()
        );
    }
}

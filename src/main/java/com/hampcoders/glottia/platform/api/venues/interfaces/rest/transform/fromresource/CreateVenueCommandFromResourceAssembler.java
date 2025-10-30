package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.CreateVenueResource;

public class CreateVenueCommandFromResourceAssembler {

    public static CreateVenueCommand toCommandFromResource(CreateVenueResource resource) {
        var address = new Address(
            resource.street(),
            resource.city(),
            resource.state(),
            resource.postalCode(),
            resource.country()
        );
        return new CreateVenueCommand(
                resource.name(),
                address,
                resource.venueTypeId()
        );
    }
}

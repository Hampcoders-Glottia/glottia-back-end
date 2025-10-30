package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ReactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.ReactivateVenueRegistrationResource;

public class ReactivateVenueRegistrationCommandFromResourceAssembler {
    public static ReactivateVenueRegistrationCommand toCommandFromResource(Long venueRegistrationId, ReactivateVenueRegistrationResource resource) {
        return new ReactivateVenueRegistrationCommand(venueRegistrationId, resource.venueId());
    }
}

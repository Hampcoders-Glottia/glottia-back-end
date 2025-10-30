package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.DeactivateVenueRegistrationResource;

public class DeactivateVenueRegistrationCommandFromResourceAssembler {
    public static DeactivateVenueRegistrationCommand toCommandFromResource(Long venueRegistrationId, DeactivateVenueRegistrationResource resource) {
        return new DeactivateVenueRegistrationCommand(venueRegistrationId, resource.venueId(), resource.reason());
    }

}

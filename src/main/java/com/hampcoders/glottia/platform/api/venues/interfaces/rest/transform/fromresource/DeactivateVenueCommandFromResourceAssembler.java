package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.DeactivateVenueResource;

public class DeactivateVenueCommandFromResourceAssembler {
    public static DeactivateVenueCommand toCommandFromResource(DeactivateVenueResource resource) {
        return new DeactivateVenueCommand(resource.venueId());
    }

}

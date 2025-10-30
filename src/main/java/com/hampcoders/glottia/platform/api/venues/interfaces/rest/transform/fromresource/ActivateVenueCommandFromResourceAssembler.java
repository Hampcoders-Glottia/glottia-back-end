package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ActivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.ActivateVenueResource;

public class ActivateVenueCommandFromResourceAssembler {
    public static ActivateVenueCommand toCommandFromResource(ActivateVenueResource resource) {
        return new ActivateVenueCommand(resource.venueId());
    }
}

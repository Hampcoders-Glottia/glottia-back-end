package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreatePartnerVenueRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.CreatePartnerVenueRegistryResource;

public class CreatePartnerVenueRegistryCommandFromResourceAssembler {
    public static CreatePartnerVenueRegistryCommand toCommandFromResource(CreatePartnerVenueRegistryResource resource) {
        return new CreatePartnerVenueRegistryCommand(new PartnerId(resource.partnerId()));
    }
}

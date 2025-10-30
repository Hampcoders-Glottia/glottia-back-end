package com.hampcoders.glottia.platform.api.venues.domain.services;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.AddVenueToPartnerRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreatePartnerVenueRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ReactivateVenueRegistrationCommand;

public interface PartnerVenueRegistryCommandService {
    Long handle(CreatePartnerVenueRegistryCommand command);
    void handle(AddVenueToPartnerRegistryCommand command);
    void handle(DeactivateVenueRegistrationCommand command);
    void handle(ReactivateVenueRegistrationCommand command);
}
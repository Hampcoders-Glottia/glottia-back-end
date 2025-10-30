package com.hampcoders.glottia.platform.api.venues.domain.services;


import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ActivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.UpdateVenueDetailsCommand;

public interface VenueCommandService {
    Long handle(CreateVenueCommand command);
    Long handle(UpdateVenueDetailsCommand command);
    void handle(DeactivateVenueCommand command);
    void handle(ActivateVenueCommand command);
}
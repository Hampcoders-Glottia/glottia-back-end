package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;

public record UpdateVenueDetailsCommand(
    Long venueId,
    String name,
    Address address,
    Long venueTypeId
) {}
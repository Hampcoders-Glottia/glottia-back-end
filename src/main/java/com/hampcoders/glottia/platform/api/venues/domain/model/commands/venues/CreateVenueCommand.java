package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;

public record CreateVenueCommand(String name, Address address, Long venueTypeId) {

}

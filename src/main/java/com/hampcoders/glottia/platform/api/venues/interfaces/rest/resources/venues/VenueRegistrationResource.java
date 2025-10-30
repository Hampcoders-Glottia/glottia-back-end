package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

public record VenueRegistrationResource(long id, Long partnerVenueRegistryId, long venueId, String registrationDate, boolean isActive) {
}
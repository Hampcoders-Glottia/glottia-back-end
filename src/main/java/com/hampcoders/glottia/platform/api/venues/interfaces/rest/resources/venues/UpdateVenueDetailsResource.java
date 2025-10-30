package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

public record UpdateVenueDetailsResource(Long venueId, String street, String city, String state, String postalCode, String country, String name, Long venueTypeId) {
}
package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

public record CreateVenueResource(String name, String street, String city, String state, String postalCode, String country, Long venueTypeId) {
}
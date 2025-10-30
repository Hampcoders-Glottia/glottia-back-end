package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

import java.util.List;

import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.VenuePromotionResource;

public record VenueResource(Long venueId, String name, String street, String city, String state, String postalCode, String country, String type, boolean isActive, List<VenuePromotionResource> promotions) {

}

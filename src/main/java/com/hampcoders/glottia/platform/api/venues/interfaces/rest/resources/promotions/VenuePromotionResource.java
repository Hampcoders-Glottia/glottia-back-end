package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

public record VenuePromotionResource(Long id, Long promotionId, Long venueId, String validFrom, String validUntil, Integer maxRedemptions, Integer currentRedemptions, Boolean isActive) {

}

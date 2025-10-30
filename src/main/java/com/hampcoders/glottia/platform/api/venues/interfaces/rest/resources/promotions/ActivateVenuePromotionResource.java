package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

/**
 * Activate venue promotion resource.
 */
public record ActivateVenuePromotionResource(Long venuePromotionId) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if venue promotion ID is null or invalid.
     */
    public ActivateVenuePromotionResource {
        if (venuePromotionId == null || venuePromotionId <= 0) throw new IllegalArgumentException("Venue promotion ID is required");
    }
}
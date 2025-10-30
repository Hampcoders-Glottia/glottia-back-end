package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

/**
 * Deactivate venue promotion resource.
 */
public record DeactivateVenuePromotionResource(Long venuePromotionId, String reason) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if venue promotion ID or reason is null/invalid.
     */
    public DeactivateVenuePromotionResource {
        if (venuePromotionId == null || venuePromotionId <= 0) throw new IllegalArgumentException("Venue promotion ID is required");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Reason is required");
    }
}
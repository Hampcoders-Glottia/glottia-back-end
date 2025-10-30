package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

import java.time.LocalDate;

/**
 * Add promotion to venue resource.
 */
public record AddPromotionToVenueResource(Long promotionId, LocalDate validFrom, LocalDate validUntil, Integer maxRedemptions) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if required fields are null or invalid.
     */
    public AddPromotionToVenueResource {
        if (promotionId == null || promotionId <= 0) throw new IllegalArgumentException("Promotion ID is required");
        if (validFrom == null) throw new IllegalArgumentException("Valid from date is required");
        if (validUntil == null) throw new IllegalArgumentException("Valid until date is required");
        if (validFrom.isAfter(validUntil)) throw new IllegalArgumentException("Valid from cannot be after valid until");
        if (maxRedemptions != null && maxRedemptions <= 0) throw new IllegalArgumentException("Max redemptions must be positive");
    }
}
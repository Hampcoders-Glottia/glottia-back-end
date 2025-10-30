package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

/**
 * Create promotion resource.
 */
public record CreatePromotionResource(Long partnerId, String name, String description, String promotionType, Integer value) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if required fields are null or invalid.
     */
    public CreatePromotionResource {
        if (partnerId <= 0) throw new IllegalArgumentException("Partner ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description is required");
        if (promotionType == null || promotionType.isBlank()) throw new IllegalArgumentException("Promotion type is required");
        if (value == null || value <= 0) throw new IllegalArgumentException("Value must be positive");
    }
}
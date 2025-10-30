package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions;

/**
 * Promotion resource.
 */
public record PromotionResource(Long id, Long partnerId, String name, String description, String promotionType, Integer value, Boolean isActive) {
}
package com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions;

public record DeactivateVenuePromotionCommand(
    Long venueId,
    Long venuePromotionId,
    String reason
) {}
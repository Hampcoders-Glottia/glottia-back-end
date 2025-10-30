package com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions;

public record ActivateVenuePromotionCommand(
    Long venueId,
    Long venuePromotionId
) {}
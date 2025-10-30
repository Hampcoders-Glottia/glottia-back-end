package com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions;

import java.time.LocalDate;

public record AddPromotionToVenueCommand(
    Long venueId, // Se usa venueId para encontrar el VenuePromotionRegistry
    Long promotionId, // El ID de la promoción
    LocalDate validFrom,
    LocalDate validUntil,
    Integer maxRedemptions
) {}
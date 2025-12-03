package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

import java.time.LocalDateTime;

public record LoyaltyTransactionResource(
    Long id,
    String type,
    Integer points,
    String description,
    Long encounterId,
    Long venueId,
    String venueName,
    LocalDateTime createdAt
) {}
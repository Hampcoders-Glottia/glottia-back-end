package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

public record LoyaltyAccountResource(
    Long id,
    Long learnerId,
    Integer points,
    Integer encountersCreated,
    Integer encountersAttended,
    Integer noShowCount
) {}

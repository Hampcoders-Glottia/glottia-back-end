package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

// Un DTO específico para el leaderboard
public record LoyaltyLeaderboardResource(
    Long learnerId,
    Integer points,
    String learnerName // Este dato vendría del Profile BC (vía ACL)
) {}

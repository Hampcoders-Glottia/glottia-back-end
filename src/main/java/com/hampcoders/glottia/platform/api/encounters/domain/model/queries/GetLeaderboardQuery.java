package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

public record GetLeaderboardQuery(Integer topN, String period) {} // topN puede ser 10, 50, etc.

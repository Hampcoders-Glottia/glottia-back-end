package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record AwardPointsCommand(
    LearnerId learnerId, 
    Integer points, 
    String reason) {}
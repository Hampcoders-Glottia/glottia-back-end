package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record PenalizeCommand(
    LearnerId learnerId, 
    Integer penalty, 
    String reason) {}
package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import java.time.LocalDateTime;

public record AutoCancelEncountersCommand(LocalDateTime thresholdTime) {}

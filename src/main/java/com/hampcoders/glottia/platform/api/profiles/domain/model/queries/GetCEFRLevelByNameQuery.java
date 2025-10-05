package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.CEFRLevels;

public record GetCEFRLevelByNameQuery(
        CEFRLevels name
) {
}
package com.hampcoders.glottia.platform.api.shared.domain.model.queries;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.CEFRLevels;

public record GetCEFRLevelByNameQuery(
        CEFRLevels name
) {
}
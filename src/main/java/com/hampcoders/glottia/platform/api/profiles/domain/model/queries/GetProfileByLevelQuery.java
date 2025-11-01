package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.CEFRLevels;

public record GetProfileByLevelQuery(
        CEFRLevels level
) {
}

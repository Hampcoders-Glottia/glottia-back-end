package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.NivelCefr;

public record GetProfileByLevel(
        NivelCefr level
) {
}

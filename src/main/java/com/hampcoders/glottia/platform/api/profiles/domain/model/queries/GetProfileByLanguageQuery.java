package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;

public record GetProfileByLanguageQuery(
        Languages language
) {
}

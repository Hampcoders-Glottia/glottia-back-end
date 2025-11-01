package com.hampcoders.glottia.platform.api.shared.domain.model.queries;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;

public record GetLanguageByNameQuery(
        Languages name
) {
}
package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import java.util.Optional;

public record SearchEncountersSimpleQuery(
    Optional<Long> languageId,
    Optional<Long> cefrLevelId,
    Optional<String> topic,
    Integer page,
    Integer size
) {
}
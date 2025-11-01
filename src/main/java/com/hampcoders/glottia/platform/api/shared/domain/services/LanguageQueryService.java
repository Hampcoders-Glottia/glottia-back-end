package com.hampcoders.glottia.platform.api.shared.domain.services;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetAllLanguagesQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetLanguageByIdQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetLanguageByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for Language entity operations
 */
public interface LanguageQueryService {
    List<Language> handle(GetAllLanguagesQuery query);
    Optional<Language> handle(GetLanguageByIdQuery query);
    Optional<Language> handle(GetLanguageByNameQuery query);
}
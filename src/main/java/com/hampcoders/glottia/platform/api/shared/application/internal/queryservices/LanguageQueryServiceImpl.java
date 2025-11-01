package com.hampcoders.glottia.platform.api.shared.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetAllLanguagesQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetLanguageByIdQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetLanguageByNameQuery;
import com.hampcoders.glottia.platform.api.shared.domain.services.LanguageQueryService;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.LanguageRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of LanguageQueryService
 * Handles language query operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Service
public class LanguageQueryServiceImpl implements LanguageQueryService {

    private final LanguageRepository languageRepository;

    public LanguageQueryServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> handle(GetAllLanguagesQuery query) {
        return this.languageRepository.findAll();
    }

    @Override
    public Optional<Language> handle(GetLanguageByIdQuery query) {
        return this.languageRepository.findById(query.languageId());
    }

    @Override
    public Optional<Language> handle(GetLanguageByNameQuery query) {
        return this.languageRepository.findByName(query.name());
    }
}
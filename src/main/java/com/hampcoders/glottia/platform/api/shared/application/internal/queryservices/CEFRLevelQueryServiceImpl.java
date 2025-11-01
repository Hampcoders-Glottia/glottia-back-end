package com.hampcoders.glottia.platform.api.shared.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetAllCEFRLevelsQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetCEFRLevelByIdQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetCEFRLevelByNameQuery;
import com.hampcoders.glottia.platform.api.shared.domain.services.CEFRLevelQueryService;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.CEFRLevelRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CEFRLevelQueryService
 * Handles CEFR level query operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Service
public class CEFRLevelQueryServiceImpl implements CEFRLevelQueryService {

    private final CEFRLevelRepository cefrLevelRepository;

    public CEFRLevelQueryServiceImpl(CEFRLevelRepository cefrLevelRepository) {
        this.cefrLevelRepository = cefrLevelRepository;
    }

    @Override
    public List<CEFRLevel> handle(GetAllCEFRLevelsQuery query) {
        return this.cefrLevelRepository.findAll();
    }

    @Override
    public Optional<CEFRLevel> handle(GetCEFRLevelByIdQuery query) {
        return this.cefrLevelRepository.findById(query.cefrLevelId());
    }

    @Override
    public Optional<CEFRLevel> handle(GetCEFRLevelByNameQuery query) {
        return this.cefrLevelRepository.findByName(query.name());
    }
}
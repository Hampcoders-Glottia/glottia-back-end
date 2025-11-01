package com.hampcoders.glottia.platform.api.shared.domain.services;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetAllCEFRLevelsQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetCEFRLevelByIdQuery;
import com.hampcoders.glottia.platform.api.shared.domain.model.queries.GetCEFRLevelByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for CEFRLevel entity operations
 */
public interface CEFRLevelQueryService {
    List<CEFRLevel> handle(GetAllCEFRLevelsQuery query);
    Optional<CEFRLevel> handle(GetCEFRLevelByIdQuery query);
    Optional<CEFRLevel> handle(GetCEFRLevelByNameQuery query);
}
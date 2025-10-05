package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllLearnersQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerByProfileIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemsByLearnerIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for Learner entity operations
 * Also manages LearnerLanguageItem queries as they belong to the Learner aggregate
 */
public interface LearnerQueryService {
    // Learner queries
    List<Learner> handle(GetAllLearnersQuery query);
    Optional<Learner> handle(GetLearnerByIdQuery query);
    Optional<Learner> handle(GetLearnerByProfileIdQuery query);
    
    // LearnerLanguageItem queries (managed through Learner aggregate)
    List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByLearnerIdQuery query);
    Optional<LearnerLanguageItem> handle(GetLearnerLanguageItemByIdQuery query);
}
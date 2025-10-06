package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllLearnersQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerByProfileIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemsByLearnerIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemsByProfileIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnersByCityQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnersByCountryQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnersByLanguageQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for Learner entity operations
 * Also manages LearnerLanguageItem queries as they belong to the Learner aggregate
 */
public interface LearnerQueryService {
    List<Learner> handle(GetAllLearnersQuery query);
    Optional<Learner> handle(GetLearnerByIdQuery query);
    Optional<Learner> handle(GetLearnerByProfileIdQuery query);
    List<Learner> handle(GetLearnersByLanguageQuery query);
    List<Learner> handle(GetLearnersByCityQuery query);
    List<Learner> handle(GetLearnersByCountryQuery query);
    
    List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByProfileIdQuery query);
    Optional<LearnerLanguageItem> handle(GetLearnerLanguageItemByIdQuery query);
    List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByLearnerIdQuery query);
}
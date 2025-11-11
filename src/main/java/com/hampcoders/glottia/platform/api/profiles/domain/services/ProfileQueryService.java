package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Query service specifically for Profile aggregate operations
 */
public interface ProfileQueryService {
    List<Profile> handle(GetAllProfilesQuery query);
    Optional<Profile> handle(GetProfileByIdQuery query);
    Optional<Profile> handle(GetProfileByEmailQuery query);
    List<Profile> handle(GetProfilesByBusinessRoleQuery query);
    List<Profile> handle(GetProfileByAgeQuery query);

    List<Partner> handle(GetAllPartnersQuery query);
    Optional<Partner> handle(GetPartnerByIdQuery query);
    Optional<Partner> handle(GetPartnerByProfileIdQuery query);
    Optional<Partner> handle(GetPartnerByTaxIdQuery query);
    Optional<Partner> handle(GetPartnerByBusinessNameQuery query);
    List<Partner> handle(GetPartnersBySubscriptionStatusQuery query);

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
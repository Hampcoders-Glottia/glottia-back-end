package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerLanguageItemRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.PartnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;
    private final LearnerRepository learnerRepository;
    private final PartnerRepository partnerRepository;
    private final LearnerLanguageItemRepository learnerLanguageItemRepository;

    public List<Profile> handle(GetAllProfilesQuery query) {
        return this.profileRepository.findAll();
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return this.profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByEmailQuery query) {
        return this.profileRepository.findByEmail(query.email());
    }

    @Override
    public List<Profile> handle(GetProfileByAgeQuery query) {
        return this.profileRepository.findByAge(query.age());
    }

    @Override
    public List<Profile> handle(GetProfilesByBusinessRoleQuery query) {
        return this.profileRepository.findByBusinessRole(query.businessRole());
    }

     @Override
    public List<Partner> handle(GetAllPartnersQuery query) {
        return this.partnerRepository.findAll();

    }

    @Override
    public Optional<Partner> handle(GetPartnerByIdQuery query) {
        return this.partnerRepository.findById(query.partnerId());

    }

    @Override
    public Optional<Partner> handle(GetPartnerByProfileIdQuery query) {
        return this.profileRepository.findPartnerByProfileId(query.profileId());
    }

    @Override
    public Optional<Partner> handle(GetPartnerByTaxIdQuery query) {
        return this.partnerRepository.findByTaxId(query.taxId());
    }

    @Override
    public Optional<Partner> handle(GetPartnerByBusinessNameQuery query) {
        return this.partnerRepository.findByBusinessNameIgnoreCase(query.businessName());
    }

    @Override
    public List<Partner> handle(GetPartnersBySubscriptionStatusQuery query) {
        return this.partnerRepository.findBySubscriptionStatusId(query.subscriptionStatus().getId());
    }

    @Override
    public List<Learner> handle(GetAllLearnersQuery query) {
        return this.learnerRepository.findAll();
    }

    @Override
    public Optional<Learner> handle(GetLearnerByIdQuery query) {
        return this.learnerRepository.findById(query.learnerId());
    }

    @Override
    public Optional<Learner> handle(GetLearnerByProfileIdQuery query) {
        return this.profileRepository.findLearnerByProfileId(query.profileId());
    }

    @Override
    public List<Learner> handle(GetLearnersByLanguageQuery query) {
        return this.learnerRepository.findByLearnerLanguageItemsLanguageId(query.languageId());
    }

    @Override
    public List<Learner> handle(GetLearnersByCityQuery query) {
        return this.learnerRepository.findByAddressCityIgnoreCase(query.city());
    }

    @Override
    public List<Learner> handle(GetLearnersByCountryQuery query) {
        // ✅ Return directo
        return this.learnerRepository.findByAddressCountryIgnoreCase(query.country());
    }

    @Override
    public List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByProfileIdQuery query) {
        return List.of(); 
    }

    @Override
    public Optional<LearnerLanguageItem> handle(GetLearnerLanguageItemByIdQuery query) {
        return this.learnerLanguageItemRepository.findById(query.learnerLanguageItemId());
    }

    @Override
    public List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByLearnerIdQuery query) {
        return this.learnerLanguageItemRepository.findByLearnerId(query.learnerId());
    }
}

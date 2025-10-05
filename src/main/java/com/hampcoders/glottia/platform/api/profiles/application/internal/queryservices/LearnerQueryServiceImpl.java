package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LearnerQueryServiceImpl implements LearnerQueryService {

    private final LearnerRepository learnerRepository;
    private final ProfileRepository profileRepository;

    public LearnerQueryServiceImpl(LearnerRepository learnerRepository, ProfileRepository profileRepository) {
        this.learnerRepository = learnerRepository;
        this.profileRepository = profileRepository;
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
        var profile = this.profileRepository.findById(query.profileId());
        return profile.map(p -> Optional.ofNullable(p.getLearner()))
                     .orElse(Optional.empty());
    }

    @Override
    public List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByLearnerIdQuery query) {
        var learner = this.learnerRepository.findById(query.learnerId());
        return learner.map(Learner::getLanguages).orElse(List.of());
    }

    @Override
    public Optional<LearnerLanguageItem> handle(GetLearnerLanguageItemByIdQuery query) {
        // Search through all learners to find the specific language item
        // This is efficient enough for most use cases, but could be optimized with a dedicated repository if needed
        return this.learnerRepository.findAll().stream()
                .flatMap(learner -> learner.getLanguages().stream())
                .filter(item -> item.getId().equals(query.learnerLanguageItemId()))
                .findFirst();
    }
}
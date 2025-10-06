package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerLanguageItemRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LearnerQueryServiceImpl implements LearnerQueryService {

    private final LearnerRepository learnerRepository;
    private final LearnerLanguageItemRepository learnerLanguageItemRepository;

    public LearnerQueryServiceImpl(LearnerRepository learnerRepository, LearnerLanguageItemRepository learnerLanguageItemRepository) {
        this.learnerRepository = learnerRepository;
        this.learnerLanguageItemRepository = learnerLanguageItemRepository;
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
        return this.learnerRepository.findByProfileId(query.profileId());
    }

    @Override
    public List<Learner> handle(GetLearnersByLanguageQuery query) {
        return this.learnerRepository.findByLearnerLanguageItemsLanguageId(query.languageId());
    }

    @Override
    public List<Learner> handle(GetLearnersByCityQuery query) {
        return this.learnerRepository.findByCityIgnoreCase(query.city());
    }

    @Override
    public List<Learner> handle(GetLearnersByCountryQuery query) {
        // ✅ Return directo
        return this.learnerRepository.findByCountryIgnoreCase(query.country());
    }

    @Override
    public List<LearnerLanguageItem> handle(GetLearnerLanguageItemsByProfileIdQuery query) {
        return this.learnerLanguageItemRepository.findByLearnerProfileId(query.profileId());
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
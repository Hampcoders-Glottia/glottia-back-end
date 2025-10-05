package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.CEFRLevelRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LanguageRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LearnerCommandServiceImpl implements LearnerCommandService {

    private final LearnerRepository learnerRepository;
    private final ProfileRepository profileRepository;
    private final LanguageRepository languageRepository;
    private final CEFRLevelRepository cefrLevelRepository;

    public LearnerCommandServiceImpl(LearnerRepository learnerRepository, ProfileRepository profileRepository,
                                   LanguageRepository languageRepository, CEFRLevelRepository cefrLevelRepository) {
        this.learnerRepository = learnerRepository;
        this.profileRepository = profileRepository;
        this.languageRepository = languageRepository;
        this.cefrLevelRepository = cefrLevelRepository;
    }

    @Override
    public Long handle(CreateLearnerCommand command) {
        var profile = this.profileRepository.findById(command.profileId())
                .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.profileId() + " does not exist"));
        
        if (profile.getLearner() != null) {
            throw new IllegalArgumentException("Profile " + command.profileId() + " is already assigned as a Learner");
        }
        
        if (profile.getPartner() != null) {
            throw new IllegalArgumentException("Profile " + command.profileId() + " is already assigned as a Partner");
        }

        var learner = new Learner(command);
        profile.assignAsLearner(learner);
        
        try {
            this.learnerRepository.save(learner);
            this.profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving learner: " + e.getMessage());
        }
        
        return learner.getId();
    }

    @Override
    public Optional<Learner> handle(AddLanguageToLearnerCommand command) {
        var learner = this.learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Learner with id " + command.learnerId() + " does not exist"));
        
        var language = this.languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " does not exist"));
        
        var cefrLevel = this.cefrLevelRepository.findById(command.cefrLevelId())
                .orElseThrow(() -> new IllegalArgumentException("CEFR Level with id " + command.cefrLevelId() + " does not exist"));
        
        learner.addLanguage(language, cefrLevel, command.isLearning());
        var updatedLearner = this.learnerRepository.save(learner);
        
        return Optional.of(updatedLearner);
    }

    @Override
    public Optional<Learner> handle(RemoveLanguageFromLearnerCommand command) {
        var learner = this.learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Learner with id " + command.learnerId() + " does not exist"));
        
        var language = this.languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " does not exist"));
        
        learner.removeLanguage(language);
        var updatedLearner = this.learnerRepository.save(learner);
        
        return Optional.of(updatedLearner);
    }

    @Override
    public Optional<Learner> handle(UpdateLearnerLanguageCommand command) {
        var learner = this.learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Learner with id " + command.learnerId() + " does not exist"));
        
        var language = this.languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " does not exist"));
        
        var cefrLevel = this.cefrLevelRepository.findById(command.cefrLevelId())
                .orElseThrow(() -> new IllegalArgumentException("CEFR Level with id " + command.cefrLevelId() + " does not exist"));
        
        // Update language by removing and re-adding with new values
        learner.removeLanguage(language);
        learner.addLanguage(language, cefrLevel, command.isLearning());
        var updatedLearner = this.learnerRepository.save(learner);
        
        return Optional.of(updatedLearner);
    }
}
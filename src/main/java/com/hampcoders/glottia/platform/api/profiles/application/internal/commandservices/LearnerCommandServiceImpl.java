package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetCEFRLevelByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLanguageByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.CEFRLevelQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LanguageQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class LearnerCommandServiceImpl implements LearnerCommandService {

    private final ProfileRepository profileRepository;
    private final LanguageQueryService languageQueryService;
    private final CEFRLevelQueryService cefrLevelQueryService;

    public LearnerCommandServiceImpl(ProfileRepository profileRepository,
                                   LanguageQueryService languageQueryService,
                                   CEFRLevelQueryService cefrLevelQueryService) {
        this.profileRepository = profileRepository;
        this.languageQueryService = languageQueryService;
        this.cefrLevelQueryService = cefrLevelQueryService;
    }

    @Override
    @Transactional
    public Long handle(CreateLearnerCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.profileId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.profileId() + " not found"));
        
        // Validate if profile is not already a learner
        if (profile.isLearner()) {
            throw new IllegalArgumentException("Profile with id " + command.profileId() + " is already a learner");
        }
        
        // Assign as learner
        profile.assignAsLearner(
            command.street(),
            command.number(),
            command.city(),
            command.postalCode(),
            command.country(),
            command.latitude(),
            command.longitude()
        );
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving learner: " + e.getMessage());
        }
        
        return profile.getLearner().getId();
    }

    @Override
    @Transactional
    public Long handle(AddLanguageToLearnerCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.learnerId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.learnerId() + " not found"));

        // Validate if profile is a learner
        if (!profile.isLearner()) {
            throw new IllegalArgumentException("Profile with id " + command.learnerId() + " is not a learner");
        }
        
        // Validate if language exists
        var language = languageQueryService.handle(new GetLanguageByIdQuery(command.languageId()))
            .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " not found"));
        
        // Validate if CEFR level exists
        var cefrLevel = cefrLevelQueryService.handle(new GetCEFRLevelByIdQuery(command.cefrLevelId()))
            .orElseThrow(() -> new IllegalArgumentException("CEFR Level with id " + command.cefrLevelId() + " not found"));
        
        // Validate if language is not already added to learner
        var learner = profile.getLearner();
        if (learner.hasLanguage(language)) {
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + 
                " is already added to learner with id " + learner.getId());
        }
        
        // Add language to learner
        learner.addLanguage(language, cefrLevel, command.isLearning());
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while adding language to learner: " + e.getMessage());
        }
        
        // Return the language item id
        var languageItem = learner.getLearnerLanguageItems().stream()
            .filter(item -> item.getLanguage().getId().equals(language.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Language item not found after saving"));
        
        return languageItem.getId();
    }

    @Override
    @Transactional
    public void handle(RemoveLanguageFromLearnerCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.learnerId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.learnerId() + " not found"));

        // Validate if profile is a learner
        if (!profile.isLearner()) {
            throw new IllegalArgumentException("Profile with id " + command.learnerId() + " is not a learner");
        }
        
        // Validate if language exists
        var language = languageQueryService.handle(new GetLanguageByIdQuery(command.languageId()))
            .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " not found"));
        
        // Validate if language is added to learner
        var learner = profile.getLearner();
        if (!learner.hasLanguage(language)) {
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + 
                " is not added to learner with id " + learner.getId());
        }
        
        // Remove language from learner
        learner.removeLanguage(language);
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while removing language from learner: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handle(UpdateLearnerLanguageCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.learnerId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.learnerId() + " not found"));

        // Validate if profile is a learner
        if (!profile.isLearner()) {
            throw new IllegalArgumentException("Profile with id " + command.learnerId() + " is not a learner");
        }
        
        // Validate if language exists
        var language = languageQueryService.handle(new GetLanguageByIdQuery(command.languageId()))
            .orElseThrow(() -> new IllegalArgumentException("Language with id " + command.languageId() + " not found"));
        
        // Validate if language is added to learner
        var learner = profile.getLearner();
        if (!learner.hasLanguage(language)) {
            throw new IllegalArgumentException("Language " + language.getStringLanguageName() + 
                " is not added to learner with id " + learner.getId());
        }
        
        // Update CEFR level if provided
        if (command.cefrLevelId() != null) {
            var cefrLevel = cefrLevelQueryService.handle(new GetCEFRLevelByIdQuery(command.cefrLevelId()))
                .orElseThrow(() -> new IllegalArgumentException("CEFR Level with id " + command.cefrLevelId() + " not found"));
            learner.updateLanguageLevel(language, cefrLevel);
        }
        
        // Update learning status if provided
        if (command.isLearning() != null) {
            learner.updateLanguageLearningStatus(language, command.isLearning());
        }
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating learner language: " + e.getMessage());
        }
    }
}
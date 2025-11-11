package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

import java.util.Optional;

/**
 * Service interface for Profile aggregate operations
 * Handles commands for the Profile aggregate root and its related entities
 */
public interface ProfileCommandService {
    // Core Profile operations
    Long handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
    
    // Learner language management operations
    Optional<LearnerLanguageItem> handle(AddLanguageToLearnerCommand command);
    void handle(RemoveLanguageFromLearnerCommand command);
    Optional<LearnerLanguageItem> handle(UpdateLearnerLanguageCommand command);
    
    Optional<Profile> handle(UpdateLearnerCommand command);

    // Partner management operations
    Optional<Profile> handle(UpdatePartnerCommand command);

}

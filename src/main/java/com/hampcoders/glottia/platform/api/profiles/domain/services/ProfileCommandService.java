package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateCompleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CompleteRegistrationCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

import java.util.Optional;

/**
 * Service interface for Profile aggregate operations
 * Handles commands for the Profile aggregate root and its related entities
 */
public interface ProfileCommandService {
    // Core Profile operations
    Long handle(CreateProfileCommand command);
    Long handle(CreateCompleteProfileCommand command); 
    Long handle(CompleteRegistrationCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
    
    // Learner language management operations
    Optional<LearnerLanguageItem> handle(AddLanguageToLearnerCommand command);
    void handle(RemoveLanguageFromLearnerCommand command);
    Optional<LearnerLanguageItem> handle(UpdateLearnerLanguageCommand command);
    
    // Partner management operations
    Optional<Profile> handle(UpdatePartnerContactCommand command);
    Optional<Profile> handle(UpdatePartnerSubscriptionCommand command);
}

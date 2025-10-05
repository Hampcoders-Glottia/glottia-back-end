package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;

import java.util.Optional;

/**
 * Service interface for Learner-specific domain operations
 */
public interface LearnerCommandService {
    Long handle(CreateLearnerCommand command);
    Optional<Learner> handle(AddLanguageToLearnerCommand command);
    Optional<Learner> handle(UpdateLearnerLanguageCommand command);
    Optional<Learner> handle(RemoveLanguageFromLearnerCommand command);
}
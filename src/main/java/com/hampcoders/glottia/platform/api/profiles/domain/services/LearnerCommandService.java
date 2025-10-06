package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;

public interface LearnerCommandService {
    Long handle(CreateLearnerCommand command);
    Long handle(AddLanguageToLearnerCommand command);
    void handle(RemoveLanguageFromLearnerCommand command);
    void handle(UpdateLearnerLanguageCommand command);
}

package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record RemoveLanguageFromLearnerCommand(
    Long learnerId,
    Long languageId
) {
}
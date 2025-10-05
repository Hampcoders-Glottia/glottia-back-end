package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdateLearnerLanguageCommand(
    Long learnerId,
    Long languageId,
    Long cefrLevelId,
    boolean isLearning
) {
}
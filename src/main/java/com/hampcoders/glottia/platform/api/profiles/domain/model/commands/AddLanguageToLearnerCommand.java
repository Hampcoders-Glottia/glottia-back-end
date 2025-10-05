package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record AddLanguageToLearnerCommand(
    Long learnerId,
    Long languageId,
    Long cefrLevelId,
    boolean isLearning
) {
}
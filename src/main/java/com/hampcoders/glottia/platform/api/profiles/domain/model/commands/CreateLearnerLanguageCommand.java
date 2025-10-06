package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record CreateLearnerLanguageCommand(
    Long languageId,
    Long cefrLevelId,
    boolean isLearning
) {
    // Validation
    public CreateLearnerLanguageCommand {
        if (languageId == null) {
            throw new IllegalArgumentException("Language ID is required");
        }
        if (cefrLevelId == null) {
            throw new IllegalArgumentException("CEFR Level ID is required");
        }
    }
}
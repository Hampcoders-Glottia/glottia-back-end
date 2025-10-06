package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

public record CreateLearnerLanguageResource(
    Long languageId,
    Long cefrLevelId,
    boolean isLearning
) {
    // Validation
    public CreateLearnerLanguageResource {
        if (languageId == null) {
            throw new IllegalArgumentException("Language ID is required");
        }
        if (cefrLevelId == null) {
            throw new IllegalArgumentException("CEFR Level ID is required");
        }
    }
}
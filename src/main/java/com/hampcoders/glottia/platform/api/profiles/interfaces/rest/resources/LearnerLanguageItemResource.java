package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record LearnerLanguageItemResource(
    Long id,
    Long learnerId,
    String language,
    String cefrLevel,
    boolean isLearning
) {
}

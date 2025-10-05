package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Update Language Resource
 * Resource for updating a learner's language proficiency
 */
public record UpdateLanguageResource(
    Long cefrLevelId,
    boolean isLearning
) {
}
package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Add Language Resource
 * Resource for adding a language to a learner
 */
public record AddLanguageResource(
    Long languageId,
    Long cefrLevelId,
    boolean isLearning
) {
}
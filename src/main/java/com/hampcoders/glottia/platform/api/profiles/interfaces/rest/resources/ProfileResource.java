package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Resource returned when retrieving a profile
 */
public record ProfileResource(
        Long id,
        String firstName,
        String lastName,
        int age,
        String email,
        String businessRole, // Single business role name
        String profileType, // "LEARNER", "PARTNER", or "UNASSIGNED"
        
        // Optional fields depending on profile type
        String street,
        String city,
        String country,
        String languageInfo, // For Learners - languages and levels
        String businessInfo, // For Partners - business name, contact info
        Long learnerId, // Only if is Learner
        Long partnerId // Only if is Partner
) {
}

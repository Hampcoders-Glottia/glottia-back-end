package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

import java.util.List;

/**
 * Command to create a complete profile with role-specific data
 * Migrated from CompleteRegistrationCommand but without IAM user creation
 */
public record CreateCompleteProfileCommand(
    String userId, 
    String firstName,
    String lastName,
    Integer age,
    String email,
    Long businessRoleId,
    
    // Learner fields (opcionales)
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    Float latitude,
    Float longitude,
    
    List<CreateLearnerLanguageCommand> languages,

    // Partner fields (opcionales)
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    String websiteUrl,
    String instagramHandle,
    Long subscriptionStatusId
) {
    // Validation
    public CreateCompleteProfileCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (businessRoleId == null) {
            throw new IllegalArgumentException("Business role is required");
        }
    }
}
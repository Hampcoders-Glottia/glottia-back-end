package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Resource for creating a complete profile with role-specific data
 */
public record CreateProfileResource(
    // Profile básico
    String firstName,
    String lastName,
    Integer age,
    String email,
    String businessRole,
    
    // Learner fields (opcionales)
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    Float latitude,
    Float longitude,
    
    // Partner fields (opcionales)
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    Long subscriptionStatusId
) {
    // Helper methods para validation
    public boolean hasLearnerData() {
        return street != null || city != null || country != null;
    }
    
    public boolean hasPartnerData() {
        return legalName != null || businessName != null || taxId != null;
    }
    
    // Validation
    public CreateProfileResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (businessRole == null || businessRole.isBlank()) {
            throw new IllegalArgumentException("Business role is required");
        }
    }
}

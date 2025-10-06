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
        
        // Optional fields depending on profile type
        String street,
        String number,  
        String city,
        String postalCode,
        String country,

        String legalName,
        String businessName,
        String taxId,
        String contactEmail,
        String contactPhone,
        String description,
        String contactPersonName,
        Long learnerId, // Only if is Learner
        Long partnerId // Only if is Partner
) {
}
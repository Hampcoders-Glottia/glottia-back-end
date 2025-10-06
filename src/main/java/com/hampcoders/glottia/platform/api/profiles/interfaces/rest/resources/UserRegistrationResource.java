package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;

public record UserRegistrationResource(
    // User credentials
    String username,
    String password,
    
    // Profile data
    String firstName,
    String lastName,
    int age,
    String email,
    BusinessRoles businessRole,
    
    // Learner specific fields (optional)
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    Float latitude,
    Float longitude,
    
    // Partner specific fields (optional)
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    String websiteUrl,
    String instagramHandle
) {
}
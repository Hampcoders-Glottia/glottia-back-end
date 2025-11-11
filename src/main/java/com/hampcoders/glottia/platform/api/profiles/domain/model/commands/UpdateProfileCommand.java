package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;


public record UpdateProfileCommand(
    Long profileId,
    String firstName,
    String lastName,
    int age,
    String email,
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
    String contactPersonName,
    String description
) {
    public UpdateProfileCommand {
        if (profileId == null || profileId <= 0) {
            throw new IllegalArgumentException("Profile ID is required and must be positive");
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
    }
}
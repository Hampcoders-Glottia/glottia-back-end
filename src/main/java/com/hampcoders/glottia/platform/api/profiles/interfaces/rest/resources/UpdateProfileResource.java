package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record UpdateProfileResource(
    String firstName,
    String lastName,
    int age,
    String email,
    // Campos opcionales para Learner (solo se usan si el perfil es Learner)
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    // Campos opcionales para Partner (solo se usan si el perfil es Partner)
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description
) {
    // Validación básica (opcional, pero recomendada para consistencia)
    public UpdateProfileResource {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }
}
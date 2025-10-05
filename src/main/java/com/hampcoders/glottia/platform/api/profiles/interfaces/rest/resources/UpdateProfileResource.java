package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Resource for updating a profile
 */
public record UpdateProfileResource(
        String firstName,
        String lastName,
        int age,
        String email,
        Long businessRoleId // Single business role ID
) {
}


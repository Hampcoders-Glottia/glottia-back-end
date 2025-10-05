package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Resource for creating a new profile
 */
public record CreateProfileResource(
    String firstName,
    String lastName,
    int age,
    String email,
    Long businessRoleId // Single business role ID
) {
}

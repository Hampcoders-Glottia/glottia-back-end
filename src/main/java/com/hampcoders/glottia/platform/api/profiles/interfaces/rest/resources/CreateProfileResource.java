package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record CreateProfileResource(
    String name,
    int age,
    String email,
    String language,
    String level,
    String country
) {
}

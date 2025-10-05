package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record CreateLearnerResource(
    Long profileId,
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    float latitude,
    float longitude
) {
}
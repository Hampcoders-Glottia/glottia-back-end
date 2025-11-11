package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdateLearnerCommand(
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
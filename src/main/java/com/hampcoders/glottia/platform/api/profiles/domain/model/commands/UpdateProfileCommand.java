package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdateProfileCommand(
        Long profileId,
        String firstName,
        String lastName,
        int age,
        String email
) {
}

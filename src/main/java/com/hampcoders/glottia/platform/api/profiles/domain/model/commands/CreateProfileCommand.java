package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record CreateProfileCommand(
        String name,
        int age,
        String email,
        String language,
        String level,
        String country
) {
}

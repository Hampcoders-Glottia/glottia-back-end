package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;


public record CreateLearnerCommand(
    Long profileId,
    String street,
    String number,
    String city,
    String postalCode,
    String country,
    Float latitude,
    Float longitude
) {
    public CreateLearnerCommand {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID is required");
        }
    }
}
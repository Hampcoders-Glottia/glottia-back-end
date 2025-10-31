package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;

public record CreateLearnerCommand(
    Long profileId,
    Address address
) {
    public CreateLearnerCommand {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID is required");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
    }
}
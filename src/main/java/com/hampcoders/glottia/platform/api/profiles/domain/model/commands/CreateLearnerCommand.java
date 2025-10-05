package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;

public record CreateLearnerCommand(
    Long profileId,
    Address address
) {
    // Learner creation with address - the Profile relationship and address are the main requirements
    // Additional language preferences can be added later via AddLanguageToLearnerCommand
}
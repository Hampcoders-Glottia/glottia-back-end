package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

import java.util.List;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;

public record CreateProfileCommand(
        String firstName,
        String lastName,
        int age,
        String email,
        List<BusinessRole> businessRoles
) {
}

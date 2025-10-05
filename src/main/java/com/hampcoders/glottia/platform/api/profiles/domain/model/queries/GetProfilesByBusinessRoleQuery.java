package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;

public record GetProfilesByBusinessRoleQuery(
        BusinessRole businessRole
) {
}
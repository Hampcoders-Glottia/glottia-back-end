package com.hampcoders.glottia.platform.api.profiles.domain.model.queries;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;

public record GetBusinessRoleByNameQuery(
        BusinessRoles name
) {
}
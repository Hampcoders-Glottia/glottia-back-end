package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllBusinessRolesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetBusinessRoleByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetBusinessRoleByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for BusinessRole entity operations
 */
public interface BusinessRoleQueryService {
    List<BusinessRole> handle(GetAllBusinessRolesQuery query);
    Optional<BusinessRole> handle(GetBusinessRoleByIdQuery query);
    Optional<BusinessRole> handle(GetBusinessRoleByNameQuery query);
}
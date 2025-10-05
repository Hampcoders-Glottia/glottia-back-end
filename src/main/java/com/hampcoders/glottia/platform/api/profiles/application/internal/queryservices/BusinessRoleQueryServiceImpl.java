package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllBusinessRolesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetBusinessRoleByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetBusinessRoleByNameQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.BusinessRoleQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.BusinessRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of BusinessRoleQueryService
 * Handles business role query operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Service
public class BusinessRoleQueryServiceImpl implements BusinessRoleQueryService {

    private final BusinessRoleRepository businessRoleRepository;

    public BusinessRoleQueryServiceImpl(BusinessRoleRepository businessRoleRepository) {
        this.businessRoleRepository = businessRoleRepository;
    }

    @Override
    public List<BusinessRole> handle(GetAllBusinessRolesQuery query) {
        return this.businessRoleRepository.findAll();
    }

    @Override
    public Optional<BusinessRole> handle(GetBusinessRoleByIdQuery query) {
        return this.businessRoleRepository.findById(query.businessRoleId());
    }

    @Override
    public Optional<BusinessRole> handle(GetBusinessRoleByNameQuery query) {
        return this.businessRoleRepository.findByRole(query.name());
    }
}
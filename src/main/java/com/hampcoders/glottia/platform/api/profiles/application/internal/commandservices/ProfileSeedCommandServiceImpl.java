package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedBusinessRolesCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedSubscriptionStatusCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileSeedCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.BusinessRoleRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.SubscriptionStatusRepository;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.CEFRLevelRepository;

@Service
public class ProfileSeedCommandServiceImpl implements ProfileSeedCommandService {

    private final BusinessRoleRepository businessRoleRepository;
    private final SubscriptionStatusRepository subscriptionStatusRepository;


    ProfileSeedCommandServiceImpl(CEFRLevelRepository cefrLevelRepository,
                                  BusinessRoleRepository businessRoleRepository,
                                  SubscriptionStatusRepository subscriptionStatusRepository) {  
        this.businessRoleRepository = businessRoleRepository;
        this.subscriptionStatusRepository = subscriptionStatusRepository;
    }

    @Override
    public void handle(SeedBusinessRolesCommand command) {
        Arrays.stream(BusinessRoles.values()).forEach(businessRole -> {
            if(!businessRoleRepository.existsByRole(businessRole)) {
                businessRoleRepository.save(new BusinessRole(BusinessRoles.valueOf(businessRole.name())));
            }
        });
    }
    @Override
    public void handle(SeedSubscriptionStatusCommand command) {
        Arrays.stream(SubscriptionStatuses.values()).forEach(status -> {
            if(!subscriptionStatusRepository.existsByName(status)) {
                subscriptionStatusRepository.save(new SubscriptionStatus(SubscriptionStatuses.valueOf(status.name())));
            }
        });
    }

}

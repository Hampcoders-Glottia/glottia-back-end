package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedBusinessRolesCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedCEFRLevelsCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedLanguagesCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedSubscriptionStatusCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.CEFRLevels;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileSeedCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.BusinessRoleRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.CEFRLevelRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LanguageRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.SubscriptionStatusRepository;

@Service
public class ProfileSeedCommandServiceImpl implements ProfileSeedCommandService {

    private final CEFRLevelRepository cefrLevelRepository;
    private final BusinessRoleRepository businessRoleRepository;
    private final LanguageRepository languageRepository;
    private final SubscriptionStatusRepository subscriptionStatusRepository;


    ProfileSeedCommandServiceImpl(CEFRLevelRepository cefrLevelRepository,
                                  BusinessRoleRepository businessRoleRepository,
                                  LanguageRepository languageRepository,
                                  SubscriptionStatusRepository subscriptionStatusRepository) {
        this.cefrLevelRepository = cefrLevelRepository;
        this.businessRoleRepository = businessRoleRepository;
        this.languageRepository = languageRepository;
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
    public void handle(SeedCEFRLevelsCommand command) {
        Arrays.stream(CEFRLevels.values()).forEach(cefrLevel -> {
            if(!cefrLevelRepository.existsByLevel(cefrLevel)) {
                cefrLevelRepository.save(new CEFRLevel(CEFRLevels.valueOf(cefrLevel.name())));
            }
        });
    }

    @Override
    public void handle(SeedLanguagesCommand command) {
        Arrays.stream(Languages.values()).forEach(language -> {
            if(!languageRepository.existsByLanguage(language)) {
                languageRepository.save(new Language(Languages.valueOf(language.name())));
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

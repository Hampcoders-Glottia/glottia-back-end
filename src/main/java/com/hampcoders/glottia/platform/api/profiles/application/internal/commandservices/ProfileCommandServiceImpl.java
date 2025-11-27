package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl.ExternalEncounterService;
import com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl.ExternalVenueService;
import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.BusinessRoleRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.LearnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.CEFRLevelRepository;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.LanguageRepository;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final LearnerRepository learnerRepository;
    private final ExternalVenueService externalVenueService;
    private final ExternalEncounterService externalEncounterService;
    private final BusinessRoleRepository businessRoleRepository;
    private final LanguageRepository languageRepository;
    private final CEFRLevelRepository cefrLevelRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository, LearnerRepository learnerRepository,
            ExternalVenueService externalVenueService, ExternalEncounterService externalEncounterService,
            BusinessRoleRepository businessRoleRepository, LanguageRepository languageRepository,
            CEFRLevelRepository cefrLevelRepository) {
        this.profileRepository = profileRepository;
        this.learnerRepository = learnerRepository;
        this.externalVenueService = externalVenueService;
        this.externalEncounterService = externalEncounterService;
        this.businessRoleRepository = businessRoleRepository;
        this.languageRepository = languageRepository;
        this.cefrLevelRepository = cefrLevelRepository;
    }

    @Override
    public Long handle(CreateProfileCommand command) {

        var fullName = command.firstName() + " " + command.lastName();
        if (this.profileRepository.existsByFullName(fullName)) {
            throw new IllegalArgumentException("Profile with full name " + fullName + " already exists");
        }

        var businessRole = businessRoleRepository.findByRole(BusinessRoles.valueOf(command.businessRole())).orElseThrow(
                () -> new IllegalStateException("Role with name " + command.businessRole() + " not found"));

        var profile = new Profile(command.firstName(), command.lastName(), command.age(), command.email(),
                businessRole);

        if (businessRole.getRole() == BusinessRoles.LEARNER) {
            profile.assignAsLearner(new Learner(new Address(command.street(), command.number(), command.city(),
                    command.postalCode(), command.country())));
        } else if (businessRole.getRole() == BusinessRoles.PARTNER) {
            profile.assignAsPartner(
                    new Partner(command.legalName(), command.businessName(), command.taxId(), command.contactEmail(),
                            command.contactPhone(), command.contactPersonName(), command.description()));
        }

        try {
            this.profileRepository.save(profile);
            if (businessRole.getRole() == BusinessRoles.PARTNER)
                externalVenueService.createVenueRegistryForPartner(profile.getPartner().getId());
            else if (businessRole.getRole() == BusinessRoles.LEARNER)
                externalEncounterService.createLoyaltyAccountForLearner(profile.getLearner().getId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving profile: " + e.getMessage());
        }
        return profile.getId();
    }

    @Override
    @Transactional
    public Optional<Profile> handle(UpdateProfileCommand command) {
        // Buscar el perfil
        Profile profile = profileRepository.findById(command.profileId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Profile with id %d does not exist", command.profileId())));

        // Validar unicidad del email (excluyendo el perfil actual)
        if (profileRepository.existsByEmailAndIdIsNot(command.email(), command.profileId())) {
            throw new IllegalArgumentException(
                    String.format("Profile with email %s already exists", command.email()));
        }

        // Validar unicidad del nombre completo (excluyendo el perfil actual)
        String fullName = command.firstName() + " " + command.lastName();
        if (profileRepository.existsByFullNameAndIdIsNot(fullName, command.profileId())) {
            throw new IllegalArgumentException(
                    String.format("Profile with full name '%s' already exists", fullName));
        }

        // Actualizar información básica
        System.out.println("DEBUG: Before updateBasicInformation, current firstName: " + profile.getFirstName());
        profile.updateBasicInformation(command.firstName(), command.lastName(), command.age(), command.email());
        System.out.println("DEBUG: After updateBasicInformation, new firstName: " + profile.getFirstName());

        if (profile.isLearner()) {
            System.out.println("DEBUG: Profile is Learner, updating learner fields");
            if (command.street() != null || command.city() != null || command.country() != null) {
                profile.updateLearner(command.street(), command.number(), command.city(), command.postalCode(),
                        command.country());
            }
        }

        System.out.println("DEBUG: Saving profile");
        profileRepository.save(profile);
        profileRepository.flush(); // Fuerza el guardado inmediato
        System.out.println("DEBUG: Profile saved and flushed successfully");

        return Optional.of(profile);
    }

    @Override
    public void handle(DeleteProfileCommand command) {
        if (!this.profileRepository.existsById(command.profileId())) {
            throw new IllegalArgumentException("Profile with id " + command.profileId() + " does not exist");
        }

        try {
            this.profileRepository.deleteById(command.profileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting profile: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<LearnerLanguageItem> handle(AddLanguageToLearnerCommand command) {
        Learner learner = learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Learner not found with ID: " + command.learnerId()));

        Language language = languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + command.languageId()));

        CEFRLevel cefrLevel = cefrLevelRepository.findById(command.cefrLevelId())
                .orElseThrow(
                        () -> new IllegalArgumentException("CEFRLevel not found with ID: " + command.cefrLevelId()));

        learner.addLanguage(language, cefrLevel, command.isLearning());

        learnerRepository.save(learner);

        // Return the newly added language item
        return learner.getLearnerLanguageItems().stream()
                .filter(item -> item.getLanguage().equals(language))
                .findFirst();
    }

    @Override
    @Transactional
    public void handle(RemoveLanguageFromLearnerCommand command) {
        Learner learner = learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Learner not found with ID: %d", command.learnerId())));

        Language language = languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Language not found with ID: %d", command.languageId())));

        if (!learner.hasLanguage(language)) {
            throw new IllegalArgumentException(
                    String.format("Learner does not have language: %s", language.getStringName()));
        }

        learner.removeLanguage(language);

        learnerRepository.save(learner);
    }

    @Override
    @Transactional
    public Optional<LearnerLanguageItem> handle(UpdateLearnerLanguageCommand command) {
        Learner learner = learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Learner not found with ID: %d", command.learnerId())));

        Language language = languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Language not found with ID: %d", command.languageId())));

        // Validar que el learner tiene este idioma
        if (!learner.hasLanguage(language)) {
            throw new IllegalArgumentException(
                    String.format("Learner does not have language: %s", language.getStringName()));
        }

        if (command.cefrLevelId() != null) {
            CEFRLevel cefrLevel = cefrLevelRepository.findById(command.cefrLevelId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("CEFR Level not found with ID: %d", command.cefrLevelId())));
            learner.updateLanguageLevel(language, cefrLevel);
        }

        if (command.isLearning() != null) {
            learner.updateLanguageLearningStatus(language, command.isLearning());
        }

        learnerRepository.save(learner);

        return learner.getLearnerLanguageItems().stream()
                .filter(item -> item.getLanguage().equals(language))
                .findFirst();
    }

    @Override
    @Transactional
    public Optional<Profile> handle(UpdateLearnerCommand command) {
        Profile profile = profileRepository.findById(command.profileId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Profile with id %d does not exist", command.profileId())));

        if (!profile.isLearner()) {
            throw new IllegalArgumentException("Profile is not assigned as a Learner");
        }

        // Actualizar información del Learner
        profile.updateLearner(command.street(), command.number(), command.city(), command.postalCode(),
                command.country());

        profileRepository.save(profile);
        return Optional.of(profile);
    }

    @Override
    @Transactional
    public Optional<Profile> handle(UpdatePartnerCommand command) {
        Profile profile = profileRepository.findById(command.profileId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Profile with id %d does not exist", command.profileId())));

        if (!profile.isPartner()) {
            throw new IllegalArgumentException("Profile is not assigned as a Partner");
        }

        // Actualizar información del Partner
        profile.updatePartner(command.legalName(), command.businessName(), command.taxId(),
                command.contactEmail(), command.contactPhone(), command.contactPersonName(), command.description());

        profileRepository.save(profile);
        return Optional.of(profile);
    }
}

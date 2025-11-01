package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CompleteRegistrationCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateCompleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;
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
import com.hampcoders.glottia.platform.api.profiles.domain.services.BusinessRoleQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.SubscriptionStatusQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import com.hampcoders.glottia.platform.api.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;
    private final LearnerRepository learnerRepository;

    private final IamContextFacade iamContextFacade;
    private final BusinessRoleQueryService businessRoleQueryService;
    private final SubscriptionStatusQueryService subscriptionStatusQueryService;
    private final BusinessRoleRepository businessRoleRepository;
    private final LanguageRepository languageRepository;
    private final CEFRLevelRepository cefrLevelRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository,
                                      LearnerRepository learnerRepository,
                                   IamContextFacade iamContextFacade,
                                   BusinessRoleQueryService businessRoleQueryService,
                                   SubscriptionStatusQueryService subscriptionStatusQueryService,
                                   BusinessRoleRepository businessRoleRepository,
                                   LanguageRepository languageRepository,
                                   CEFRLevelRepository cefrLevelRepository) {
        this.profileRepository = profileRepository;
        this.learnerRepository = learnerRepository;
        this.iamContextFacade = iamContextFacade;
        this.businessRoleQueryService = businessRoleQueryService;
        this.subscriptionStatusQueryService = subscriptionStatusQueryService;
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
        
        var businessRole = businessRoleRepository.findByRole(BusinessRoles.valueOf(command.businessRole())).orElseThrow(() -> new IllegalStateException("Role with name " + command.businessRole() + " not found"));


        var profile = new Profile(command.firstName(),command.lastName(),command.age(), command.email(), businessRole);

        if(businessRole.getRole() == BusinessRoles.Learner) {
            profile.assignAsLearner(new Learner(new Address(command.street(), command.number(), command.city(), command.postalCode(), command.country(), command.latitude(), command.longitude())));
        } else if (businessRole.getRole() == BusinessRoles.Partner) {
            profile.assignAsPartner(new Partner(command.legalName(), command.businessName(), command.taxId(), command.contactEmail(), command.contactPhone(), command.contactPersonName(), command.description()));
        }

        try {
            this.profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving profile: " + e.getMessage());
        }
        return profile.getId();
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        var profileId = command.profileId();
        var fullName = command.firstName() + " " + command.lastName();
        if (this.profileRepository.existsByFullNameAndIdIsNot(fullName, profileId)) {
            throw new IllegalArgumentException("Profile with full name " + fullName + " already exists");
        }

        // If the profile does not exist, throw an exception
        if (!this.profileRepository.existsById(profileId)) {
            throw new IllegalArgumentException("Profile with id " + profileId + " does not exist");
        }

        var profileToUpdate = this.profileRepository.findById(profileId).get();
        profileToUpdate.updateInformation(command);

        try {
            var updatedProfile = this.profileRepository.save(profileToUpdate);
            return Optional.of(updatedProfile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating profile: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteProfileCommand command) {
        // If the profile does not exist, throw an exception
        if (!this.profileRepository.existsById(command.profileId())) {
            throw new IllegalArgumentException("Profile with id " + command.profileId() + " does not exist");
        }

        // Try to delete the profile, if an error occurs, throw an exception
        try {
            this.profileRepository.deleteById(command.profileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting profile: " + e.getMessage());
        }
    }

    // TODO: Implement learner and partner management operations
    // These methods are placeholders until the entity relationships are properly configured
    
    @Override
    @Transactional
    public Optional<LearnerLanguageItem> handle(AddLanguageToLearnerCommand command) {
        Learner learner = learnerRepository.findById(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Learner not found with ID: " + command.learnerId()));

        Language language = languageRepository.findById(command.languageId())
                .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + command.languageId()));

        CEFRLevel cefrLevel = cefrLevelRepository.findById(command.cefrLevelId())
                .orElseThrow(() -> new IllegalArgumentException("CEFRLevel not found with ID: " + command.cefrLevelId()));

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
        // TODO: Implement when Learner entity has proper language management methods
        throw new UnsupportedOperationException("RemoveLanguageFromLearnerCommand not yet implemented");
    }

    @Override
    @Transactional
    public Optional<LearnerLanguageItem> handle(UpdateLearnerLanguageCommand command) {
        // TODO: Implement when Learner entity has proper language management methods
        throw new UnsupportedOperationException("UpdateLearnerLanguageCommand not yet implemented");
    }

    @Override
    @Transactional
    public Optional<Profile> handle(UpdatePartnerContactCommand command) {
        // TODO: Implement when Partner entity has proper contact management methods
        throw new UnsupportedOperationException("UpdatePartnerContactCommand not yet implemented");
    }

    @Override
    @Transactional
    public Optional<Profile> handle(UpdatePartnerSubscriptionCommand command) {
        // TODO: Implement when Partner entity has proper subscription management methods
        throw new UnsupportedOperationException("UpdatePartnerSubscriptionCommand not yet implemented");
    }

    @Override
    public Long handle(CreateCompleteProfileCommand command) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

    @Override
    public Long handle(CompleteRegistrationCommand command) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }
}

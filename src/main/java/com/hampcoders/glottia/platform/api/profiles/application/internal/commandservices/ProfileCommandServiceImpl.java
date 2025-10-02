package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Long handle(CreateProfileCommand command) {
        var name = command.name();
        if (this.profileRepository.existsByFullName(name)) {
            throw new IllegalArgumentException("Profile with full name " + name + " already exists");
        }
        var profile = new Profile(command);
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
        var name = command.name();
        if (this.profileRepository.existsByFullNameAndIdIsNot(name, profileId)) {
            throw new IllegalArgumentException("Profile with full name " + name + " already exists");
        }

        // If the profile does not exist, throw an exception
        if (!this.profileRepository.existsById(profileId)) {
            throw new IllegalArgumentException("Profile with id " + profileId + " does not exist");
        }

        var profileToUpdate = this.profileRepository.findById(profileId).get();
        profileToUpdate.updateInformation(
                command.name(),
                command.age(),
                command.email(),
                command.language(),
                command.level(),
                command.country()
        );

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
}

package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query) {
        return this.profileRepository.findAll();
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return this.profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByEmailQuery query) {
        return this.profileRepository.findByEmail(query.email());
    }

    @Override
    public List<Profile> handle(GetProfileByLanguageQuery query) {
        // Filter profiles that have learners with the specified language
        return this.profileRepository.findAll().stream()
                .filter(profile -> profile.getLearner() != null && 
                    profile.getLearner().getLanguages().stream()
                        .anyMatch(lang -> lang.getLanguage().getLanguage().equals(query.language())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Profile> handle(GetProfileByLevelQuery query) {
        // Filter profiles that have learners with the specified CEFR level
        return this.profileRepository.findAll().stream()
                .filter(profile -> profile.getLearner() != null && 
                    profile.getLearner().getLanguages().stream()
                        .anyMatch(lang -> lang.getCefrLevel().getLevel().equals(query.level())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Profile> handle(GetProfileByAgeQuery query) {
        return this.profileRepository.findByAge(query.age());
    }

    @Override
    public List<Profile> handle(GetProfilesByBusinessRoleQuery query) {
        return this.profileRepository.findByBusinessRolesContaining(query.businessRole());
    }
}

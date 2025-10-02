package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllProfilesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByLanguageQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByAgeQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Profile> handle(GetProfileByLanguageQuery query) {
        return this.profileRepository.findByLanguage(query.language());
    }

    @Override
    public List<Profile> handle(GetProfileByLevel query) {
        return this.profileRepository.findByLevel(query.level());
    }

    @Override
    public List<Profile> handle(GetProfileByAgeQuery query) {
        return this.profileRepository.findByAge(query.age());
    }
}

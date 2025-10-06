package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Query service specifically for Profile aggregate operations
 */
public interface ProfileQueryService {
    List<Profile> handle(GetAllProfilesQuery query);
    Optional<Profile> handle(GetProfileByIdQuery query);
    Optional<Profile> handle(GetProfileByEmailQuery query);
    List<Profile> handle(GetProfilesByBusinessRoleQuery query);
    List<Profile> handle(GetProfileByAgeQuery query);
}
package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllProfilesQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByLanguageQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByLevel;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByAgeQuery;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    List<Profile> handle(GetAllProfilesQuery query);
    Optional<Profile> handle(GetProfileByIdQuery query);
    List<Profile> handle(GetProfileByLanguageQuery query);
    List<Profile> handle(GetProfileByLevel query);
    List<Profile> handle(GetProfileByAgeQuery query);
}
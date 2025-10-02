package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.ProfileResource;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
                entity.getId(),
                entity.getFullName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getLanguage(),
                entity.getLevel(),
                entity.getCountry()
        );
    }

    public static List<ProfileResource> toResourceListFromEntities(List<Profile> entities) {
        return entities.stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
    }
}

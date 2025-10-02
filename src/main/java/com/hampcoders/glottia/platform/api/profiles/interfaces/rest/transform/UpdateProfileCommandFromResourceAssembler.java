package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
                profileId,
                resource.name(),
                resource.age(),
                resource.email(),
                resource.language(),
                resource.level(),
                resource.country()
        );
    }
}


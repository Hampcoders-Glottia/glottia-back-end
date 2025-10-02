package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.name(),
                resource.age(),
                resource.email(),
                resource.language(),
                resource.level(),
                resource.country()
        );
    }
}
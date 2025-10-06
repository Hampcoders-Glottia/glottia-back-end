package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CreateLearnerResource;

public class CreateLearnerCommandFromResourceAssembler {

    public static CreateLearnerCommand toCommandFromResource(CreateLearnerResource resource) {
        return new CreateLearnerCommand(
            resource.profileId(),
            resource.street(),
            resource.number(),
            resource.city(),
            resource.postalCode(),
            resource.country(),
            resource.latitude(),
            resource.longitude()
        );
    }
}
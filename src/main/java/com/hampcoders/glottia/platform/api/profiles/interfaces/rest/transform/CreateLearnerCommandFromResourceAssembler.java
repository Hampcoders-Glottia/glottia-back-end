package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreateLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CreateLearnerResource;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Address;

public class CreateLearnerCommandFromResourceAssembler {

    public static CreateLearnerCommand toCommandFromResource(CreateLearnerResource resource) {
        var address = new Address(
                resource.address().street(),
                resource.address().number(),
                resource.address().city(),
                resource.address().postalCode(),
                resource.address().country()
        );

        return new CreateLearnerCommand(
            resource.profileId(),
            address
        );
    }
}
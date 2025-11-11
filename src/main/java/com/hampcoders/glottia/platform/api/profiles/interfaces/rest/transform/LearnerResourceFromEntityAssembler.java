package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import java.util.Collections;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.LearnerResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.AddressResource;

public class LearnerResourceFromEntityAssembler {

    public static LearnerResource toResource(Learner entity) {
        var addressResource = new AddressResource(
            entity.getAddress().street(),
            entity.getAddress().number(),
            entity.getAddress().city(),
            entity.getAddress().postalCode(),
            entity.getAddress().country()
        );

        return new LearnerResource(
            entity.getId(),
            addressResource,
            Collections.emptyList() // TODO: Implement language management
        );
    }
}
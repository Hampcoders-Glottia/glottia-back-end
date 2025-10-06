package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import java.util.Collections;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.LearnerResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.AddressResource;

public class LearnerResourceFromEntityAssembler {

    public static LearnerResource toResource(Learner entity) {
        AddressResource addressResource = new AddressResource(
            entity.getStreet(),
            entity.getNumber(),
            entity.getCity(),
            entity.getPostalCode(),
            entity.getCountry(),
            entity.getLatitude(),
            entity.getLongitude()
        );

        return new LearnerResource(
            entity.getId(),
            addressResource,
            Collections.emptyList() // TODO: Implement language management
        );
    }
}
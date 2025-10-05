package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import java.util.stream.Collectors;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.LearnerResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.AddressResource;

public class LearnerResourceFromEntityAssembler {

    public static LearnerResource toResource(Learner entity) {
        AddressResource addressResource = null;
        if (entity.getAddress() != null) {
            addressResource = new AddressResource(
                entity.getAddress().street(),
                entity.getAddress().number(),
                entity.getAddress().city(),
                entity.getAddress().postalCode(),
                entity.getAddress().country(),
                entity.getAddress().latitude(),
                entity.getAddress().longitude()
            );
        }

        return new LearnerResource(
            entity.getId(),
            addressResource,
            entity.getLanguages().stream()
                .map(LearnerLanguageItemResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList())
        );
    }
}
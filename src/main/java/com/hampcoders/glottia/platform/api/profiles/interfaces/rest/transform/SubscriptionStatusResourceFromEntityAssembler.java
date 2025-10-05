package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.SubscriptionStatus;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.SubscriptionStatusResource;

public class SubscriptionStatusResourceFromEntityAssembler {

    public static SubscriptionStatusResource toResource(SubscriptionStatus entity) {
        return new SubscriptionStatusResource(
            entity.getId(),
            entity.getStringStatusName()
        );
    }
}

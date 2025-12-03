package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.LoyaltyTransactionResource;

public class LoyaltyTransactionResourceFromEntityAssembler {

    public static LoyaltyTransactionResource toResourceFromEntity(LoyaltyTransaction entity) {
        return new LoyaltyTransactionResource(
            entity.getId(),
            entity.getType().name(),
            entity.getPoints(),
            entity.getDescription(),
            entity.getEncounterId(),
            entity.getVenueId() != null ? entity.getVenueId().venueId() : null,
            entity.getVenueName(),
            entity.getCreatedAt()
        );
    }
}
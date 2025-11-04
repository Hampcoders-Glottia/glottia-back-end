package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.LoyaltyAccountResource;

public class LoyaltyAccountResourceFromEntityAssembler {
    public static LoyaltyAccountResource toResourceFromEntity(LoyaltyAccount entity) {
        return new LoyaltyAccountResource(
            entity.getId(),
            entity.getLearnerId().learnerId(),
            entity.getPoints(),
            entity.getEncountersCreated(),
            entity.getEncountersAttended(),
            entity.getNoShowCount()
        );
    }
}

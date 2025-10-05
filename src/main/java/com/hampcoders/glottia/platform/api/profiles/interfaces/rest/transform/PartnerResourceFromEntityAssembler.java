package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.PartnerResource;

public class PartnerResourceFromEntityAssembler {

    public static PartnerResource toResource(Partner entity) {
        return new PartnerResource(
            entity.getId(),
            entity.getLegalName(),
            entity.getBusinessName(),
            entity.getTaxId(),
            entity.getContactEmail(),
            entity.getContactPhone(),
            entity.getContactPersonName(),
            entity.getDescription(),
            entity.getWebsiteUrl(),
            entity.getInstagramHandle(),
            entity.getSubscriptionStatus().getStringStatusName() // Use the available method
        );
    }
}

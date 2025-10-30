package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

public record CreatePartnerVenueRegistryCommand(PartnerId partnerId) {
    public CreatePartnerVenueRegistryCommand {
        if (partnerId == null) {
            throw new IllegalArgumentException("PartnerId cannot be null");
        }
    }
}
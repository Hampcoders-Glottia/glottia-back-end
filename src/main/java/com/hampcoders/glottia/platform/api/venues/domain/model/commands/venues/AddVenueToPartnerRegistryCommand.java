package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

import java.time.LocalDateTime;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

public record AddVenueToPartnerRegistryCommand (
    PartnerId partnerId,
    Long venueId,
    LocalDateTime registrationDate,
    Boolean isActive

) {
    public AddVenueToPartnerRegistryCommand {
        if (partnerId.partnerId() == null || partnerId.partnerId() <= 0) {
            throw new IllegalArgumentException("Partner ID must be positive");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be positive");
        }
        if (registrationDate == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
        if (isActive == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }
    }
}
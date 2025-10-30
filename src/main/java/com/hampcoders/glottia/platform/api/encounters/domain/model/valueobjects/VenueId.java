package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record VenueId(Long venueId) {
    public VenueId {
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be a positive number.");
        }
    }
     public VenueId() { this(0L); }
}
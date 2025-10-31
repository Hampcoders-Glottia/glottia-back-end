package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a Venue ID.
 * @summary
 * Represents the unique identifier for a venue in the system.
 * This class is immutable and ensures that the venue ID is a positive number.
 * @param venueId The unique identifier of the venue.
 * @see IllegalArgumentException
 */
@Embeddable
public record VenueId(Long venueId) {
    public VenueId {
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be a positive number.");
        }
    }
    
    public VenueId() { this(0L); }

    public static VenueId of(Long venueId) {
        return new VenueId(venueId);
    }

    @Override
    public String toString() {
        return venueId.toString();
    }
}
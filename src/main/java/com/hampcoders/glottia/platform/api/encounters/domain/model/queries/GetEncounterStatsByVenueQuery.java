package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import java.time.LocalDate;

/**
 * Query to get encounter statistics by venue for a date range.
 * Used by Venues BC via ACL for statistics dashboard.
 * 
 * @param venueId   The venue ID
 * @param startDate Start date (inclusive)
 * @param endDate   End date (inclusive)
 */
public record GetEncounterStatsByVenueQuery(
    Long venueId,
    LocalDate startDate,
    LocalDate endDate
) {
    public GetEncounterStatsByVenueQuery {
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be a positive number");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
}
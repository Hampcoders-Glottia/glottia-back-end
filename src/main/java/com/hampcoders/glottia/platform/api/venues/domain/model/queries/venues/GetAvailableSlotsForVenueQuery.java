package com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues;

import java.time.LocalDate;

/**
 * Query to get available slots for a specific venue.
 * Supports filtering by date range or fetching all future available slots.
 * 
 * @param venueId   The venue ID (required)
 * @param startDate Start date filter (optional, defaults to today)
 * @param endDate   End date filter (optional, defaults to null = no limit)
 */
public record GetAvailableSlotsForVenueQuery(
    Long venueId,
    LocalDate startDate,
    LocalDate endDate
) {
    public GetAvailableSlotsForVenueQuery {
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be a positive number");
        }
        // Default startDate to today if not provided
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        // Validate date range if both provided
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    /**
     * Factory method for fetching all future slots
     */
    public static GetAvailableSlotsForVenueQuery allFutureSlots(Long venueId) {
        return new GetAvailableSlotsForVenueQuery(venueId, LocalDate.now(), null);
    }

    /**
     * Factory method for fetching slots on a specific date
     */
    public static GetAvailableSlotsForVenueQuery forSpecificDate(Long venueId, LocalDate date) {
        return new GetAvailableSlotsForVenueQuery(venueId, date, date);
    }

    /**
     * Factory method for fetching slots in a date range
     */
    public static GetAvailableSlotsForVenueQuery forDateRange(Long venueId, LocalDate startDate, LocalDate endDate) {
        return new GetAvailableSlotsForVenueQuery(venueId, startDate, endDate);
    }
}
package com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Query to get encounter statistics for a specific venue.
 * Supports filtering by month/year or last N days.
 * 
 * @param venueId   The venue ID (required)
 * @param startDate Start date of the period
 * @param endDate   End date of the period
 */
public record GetVenueEncounterStatisticsQuery(
    Long venueId,
    LocalDate startDate,
    LocalDate endDate
) {
    public GetVenueEncounterStatisticsQuery {
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

    /**
     * Factory method for last N days
     */
    public static GetVenueEncounterStatisticsQuery forLastDays(Long venueId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        return new GetVenueEncounterStatisticsQuery(venueId, startDate, endDate);
    }

    /**
     * Factory method for specific month and year
     */
    public static GetVenueEncounterStatisticsQuery forMonth(Long venueId, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return new GetVenueEncounterStatisticsQuery(venueId, startDate, endDate);
    }
}
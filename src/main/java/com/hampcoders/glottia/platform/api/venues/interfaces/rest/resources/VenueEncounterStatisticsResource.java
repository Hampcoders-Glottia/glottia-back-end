package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource representing complete venue encounter statistics.
 */
public record VenueEncounterStatisticsResource(
    Long venueId,
    LocalDate startDate,
    LocalDate endDate,
    List<WeeklyEncounterStatResource> weeklyStatistics,
    TotalsResource totals
) {
    public record TotalsResource(
        long totalScheduled,
        long totalCompleted
    ) {}
}
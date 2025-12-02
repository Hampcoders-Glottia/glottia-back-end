package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * Resource representing daily encounter statistics for a venue.
 */
public record DailyEncounterStatResource(
    LocalDate date,
    String dayOfWeekFormatted,  // e.g., "Lunes 01 de Diciembre"
    int weekNumber,
    long scheduledCount,
    long completedCount
) {}
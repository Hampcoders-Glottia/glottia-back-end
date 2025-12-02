package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources;

import java.util.List;

/**
 * Resource representing weekly encounter statistics for a venue.
 */
public record WeeklyEncounterStatResource(
    int weekNumber,
    String weekLabel,  // e.g., "Semana 1 (01 dic - 07 dic)"
    List<DailyEncounterStatResource> dailyStats,
    long totalScheduled,
    long totalCompleted
) {}
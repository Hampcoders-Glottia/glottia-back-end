package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources;

import java.util.List;

/**
 * Resource representing weekly encounter statistics.
 */
public record WeeklyEncounterStatResource(
    int weekNumber,
    String weekLabel,  // e.g., "Semana 1 (01 Dic - 07 Dic)"
    List<DailyEncounterStatResource> dailyStats,
    long totalScheduled,
    long totalCompleted
) {}
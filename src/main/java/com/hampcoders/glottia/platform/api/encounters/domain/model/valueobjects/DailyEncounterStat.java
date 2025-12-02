package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * Value object representing daily encounter statistics.
 * Used internally within the Encounters BC.
 */
public record DailyEncounterStat(
    LocalDate date,
    long scheduledCount,
    long completedCount
) {
    public DailyEncounterStat {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (scheduledCount < 0 || completedCount < 0) {
            throw new IllegalArgumentException("Counts cannot be negative");
        }
    }
}
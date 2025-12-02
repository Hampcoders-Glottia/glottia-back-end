package com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Query to find all available tables at a venue for a specific date and time.
 * Used by the Encounters BC (via ACL) for auto-assignment of tables to
 * encounters.
 */
public record GetAvailableTablesAtTimeQuery(
        Long venueId,
        LocalDate date,
        LocalTime startTime) {
}

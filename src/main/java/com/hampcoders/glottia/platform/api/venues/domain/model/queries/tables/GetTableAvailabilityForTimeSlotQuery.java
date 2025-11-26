package com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Query to check if a specific table has availability at a given time slot.
 * Used by the Encounters BC (via ACL) to validate table availability before
 * creating encounters.
 */
public record GetTableAvailabilityForTimeSlotQuery(
        Long tableId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime) {
}

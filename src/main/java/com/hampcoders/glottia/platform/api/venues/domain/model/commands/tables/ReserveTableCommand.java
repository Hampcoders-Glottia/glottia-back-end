package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to reserve a table for a specific date and time slot
 * Typically triggered by Encounters BC
 */
public record ReserveTableCommand(
        Long tableId,
        LocalDate reservationDate,
        LocalTime startHour, // Start time of 2-hour slot
        LocalTime endHour // End time of 2-hour slot
/* Long encounterId */
) {
    public ReserveTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null");
        }
        if (reservationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot reserve past dates");
        }
        if (startHour == null) {
            throw new IllegalArgumentException("Start hour cannot be null");
        }
        if (endHour == null) {
            throw new IllegalArgumentException("End hour cannot be null");
        }
        /*
         * if (encounterId == null || encounterId <= 0) {
         * throw new IllegalArgumentException("Encounter ID must be positive");
         * }
         */
    }
}

package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;

/**
 * Command to reserve a table for a specific date
 * Typically triggered by Encounters BC
 */
public record ReserveTableCommand(
    Long tableId,
    LocalDate reservationDate
    /*Long encounterId*/
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
        /*if (encounterId == null || encounterId <= 0) {
            throw new IllegalArgumentException("Encounter ID must be positive");
        }*/
    }
}
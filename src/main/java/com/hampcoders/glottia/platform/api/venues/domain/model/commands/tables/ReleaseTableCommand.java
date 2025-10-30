package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;

/**
 * Command to release a table reservation for a specific date
 */
public record ReleaseTableCommand(
    Long tableId,
    LocalDate reservationDate
) {
    public ReleaseTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null");
        }
    }
}
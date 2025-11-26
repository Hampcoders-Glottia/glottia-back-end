package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to release a table reservation for a specific date and time slot
 */
public record ReleaseTableCommand(
        Long tableId,
        LocalDate reservationDate,
        LocalTime startHour,
        LocalTime endHour) {
    public ReleaseTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (reservationDate == null) {
            throw new IllegalArgumentException("Reservation date cannot be null");
        }
        if (startHour == null) {
            throw new IllegalArgumentException("Start hour cannot be null");
        }
        if (endHour == null) {
            throw new IllegalArgumentException("End hour cannot be null");
        }
    }
}

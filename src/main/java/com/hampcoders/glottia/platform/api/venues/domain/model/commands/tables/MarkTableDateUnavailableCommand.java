package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to mark a specific date and time slot as unavailable for a table
 */
public record MarkTableDateUnavailableCommand(
        Long tableId,
        LocalDate date,
        LocalTime startHour,
        LocalTime endHour) {
    public MarkTableDateUnavailableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (startHour == null) {
            throw new IllegalArgumentException("Start hour cannot be null");
        }
        if (endHour == null) {
            throw new IllegalArgumentException("End hour cannot be null");
        }
    }
}

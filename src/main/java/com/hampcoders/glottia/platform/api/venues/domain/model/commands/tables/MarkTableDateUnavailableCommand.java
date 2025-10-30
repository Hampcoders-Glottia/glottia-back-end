package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.LocalDate;

/**
 * Command to mark a specific date as unavailable for a table
 */
public record MarkTableDateUnavailableCommand(
    Long tableId,
    LocalDate date
) {
    public MarkTableDateUnavailableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }
}
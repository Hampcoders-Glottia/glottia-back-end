package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

/**
 * Command to mark a table as unavailable (general status)
 */
public record MarkTableUnavailableCommand(
    Long tableId
) {
    public MarkTableUnavailableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
    }
}

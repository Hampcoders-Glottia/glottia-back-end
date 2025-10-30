package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

/**
 * Command to mark a table as available
 */
public record MarkTableAvailableCommand(
    Long tableId
) {
    public MarkTableAvailableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
    }
}

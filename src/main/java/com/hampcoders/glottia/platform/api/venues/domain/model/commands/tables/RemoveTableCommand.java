package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

/**
 * Command to remove a table from the registry
 * Only allowed if no active reservations exist
 */
public record RemoveTableCommand(
    Long tableId
) {
    public RemoveTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
    }
}
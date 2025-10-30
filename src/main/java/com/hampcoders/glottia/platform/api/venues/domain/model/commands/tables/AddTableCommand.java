package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

/**
 * Command to add a new table to a venue's registry
 */
public record AddTableCommand(
    Long tableId,
    String tableNumber,
    Integer capacity,
    String tableType,
    String tableStatus
) {
    public AddTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (tableNumber == null || tableNumber.isBlank()) {
            throw new IllegalArgumentException("Table number cannot be null or empty");
        }
        if (capacity == null || capacity < 4 || capacity > 6) {
            throw new IllegalArgumentException("Capacity must be between 4 and 6");
        }
        if (tableType == null || tableType.isBlank()) {
            throw new IllegalArgumentException("Table type cannot be null or empty");
        }
        if (tableStatus == null || tableStatus.isBlank()) {
            throw new IllegalArgumentException("Table status cannot be null or empty");
        }
    }
}


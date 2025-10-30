package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.util.Optional;

/**
 * Command to update table details
 * All fields are optional - only provided fields will be updated
 */
public record UpdateTableCommand(
    Long tableId,
    Optional<Integer> capacity,
    Optional<Long> tableTypeId
) {
    public UpdateTableCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be positive");
        }
        if (capacity == null || tableTypeId == null) {
            throw new IllegalArgumentException("Optional fields cannot be null (use Optional.empty())");
        }
        // Validate capacity if present
        capacity.ifPresent(cap -> {
            if (cap < 4 || cap > 6) {
                throw new IllegalArgumentException("Capacity must be between 4 and 6");
            }
        });
    }
}

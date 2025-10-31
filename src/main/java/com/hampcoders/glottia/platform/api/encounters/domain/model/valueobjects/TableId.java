package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a Table ID.
 * @summary
 * Represents the unique identifier for a table in the system.
 * This class is immutable and ensures that the table ID is a positive number.
 * @param tableId The unique identifier of the table.
 * @see IllegalArgumentException
 */
@Embeddable
public record TableId(Long tableId) {
    public TableId {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be a positive number.");
        }
    }
    
    public TableId() { this(0L); }

    public static TableId of(Long tableId) {
        return new TableId(tableId);
    }

    @Override
    public String toString() {
        return tableId.toString();
    }

}
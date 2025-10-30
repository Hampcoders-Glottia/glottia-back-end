package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TableId(Long tableId) {
    public TableId {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be a positive number.");
        }
    }
     public TableId() { this(0L); }
}
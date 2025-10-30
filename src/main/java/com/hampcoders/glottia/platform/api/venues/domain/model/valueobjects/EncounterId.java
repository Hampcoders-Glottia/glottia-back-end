package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * EncounterId value object representing the unique identifier for an encounter from Encounters and Engagement Management Bounded Context.
 * Cross-context reference to Encounters and Engagement Management Bounded Context
 */
@Embeddable
public record EncounterId(Long encounterId) {

    public EncounterId {
        if (encounterId == null) {
            throw new IllegalArgumentException("Encounter ID cannot be null");
        }
        if (encounterId <= 0) {
            throw new IllegalArgumentException("Encounter ID must be positive");
        }
    }

    public EncounterId() {
        this(0L);
    }

    public static EncounterId of(Long encounterId) {
        return new EncounterId(encounterId);
    }

    @Override
    public String toString() {
        return encounterId.toString();
    }

}

package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * PartnerId Value Object
 * Represents the identifier of a Partner from Profiles & Preferences Bounded Context
 * Cross-context reference to Profiles & Preferences Bounded Context
 */
@Embeddable
public record PartnerId(Long partnerId) {
    
    public PartnerId {
        if (partnerId == null) {
            throw new IllegalArgumentException("Partner ID cannot be null");
        }
        if (partnerId <= 0) {
            throw new IllegalArgumentException("Partner ID must be positive");
        }
    }
    
    public PartnerId() {
        this(0L);
    }
    
    public static PartnerId of(Long partnerId) {
        return new PartnerId(partnerId);
    }
    
    @Override
    public String toString() {
        return partnerId.toString();
    }
}
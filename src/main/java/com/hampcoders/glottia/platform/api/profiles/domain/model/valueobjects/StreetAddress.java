package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StreetAddress(String street) {
    public StreetAddress() {
        this(null);
    }
    public StreetAddress {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }
    }
}
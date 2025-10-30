package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Address Value Object
 * Represents a physical address
 * Includes street, city, state, postal code, and country
 */

@Embeddable
public record Address(
    String street,
    String city,
    String state,
    String postalCode,
    String country
) {
    
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }
    
    public Address() {
        this("", "", "", "", "");
    }
    
    /**
     * Returns formatted address for display
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(street);
        if (city != null && !city.isBlank()) {
            sb.append(", ").append(city);
        }
        if (state != null && !state.isBlank()) {
            sb.append(", ").append(state);
        }
        if (postalCode != null && !postalCode.isBlank()) {
            sb.append(" ").append(postalCode);
        }
        if (country != null && !country.isBlank()) {
            sb.append(", ").append(country);
        }
        return sb.toString();
    }
}
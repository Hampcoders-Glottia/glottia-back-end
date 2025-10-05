package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String street,String number, String city, String postalCode, String country, float latitude, float longitude ) {

    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number cannot be null or empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be null or empty");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }

    public Address() {
        this("", "", "", "", "", 0.0f, 0.0f);
    }
}
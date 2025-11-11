package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing an address.
 * @summarry
 * It encapsulates the various components that make up a physical address.
 * @field street The street name of the address.
 * @field number The building or house number.
 * @field city The city of the address.
 * @field postalCode The postal code of the address.
 * @field country The country of the address.
 * @field latitude The latitude coordinate of the address.
 * @field longitude The longitude coordinate of the address.
 */
@Embeddable
public record Address(String street,String number, String city, String postalCode, String country) {

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
        this("", "", "", "", "");
    }

}
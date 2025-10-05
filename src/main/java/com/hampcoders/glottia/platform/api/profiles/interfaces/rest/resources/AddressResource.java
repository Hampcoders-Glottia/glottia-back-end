package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

/**
 * Address Resource
 * Resource representation of the Address value object
 */
public record AddressResource(
        String street,
        String number,
        String city,
        String postalCode,
        String country,
        float latitude,
        float longitude
) {
}
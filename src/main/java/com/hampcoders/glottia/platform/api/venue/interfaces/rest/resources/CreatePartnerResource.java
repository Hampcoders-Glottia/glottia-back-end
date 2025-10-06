package com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources;

import java.util.List;

/**
 * Recurso de entrada para crear un nuevo Partner.
 */
public record CreatePartnerResource(
        Long userId,
        String legalName,
        List<CreateVenueResource> initialVenues
) {
}


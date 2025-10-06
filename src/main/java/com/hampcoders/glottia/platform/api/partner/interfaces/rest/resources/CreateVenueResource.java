package com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources;

import java.util.List;

/**
 * Recurso de entrada para crear un Venue.
 */
public record CreateVenueResource(
        String name,
        String address,
        List<CreateTableResource> initialTables
) {
}


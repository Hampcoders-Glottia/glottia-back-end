package com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources;

import java.util.List;

/**
 * Recurso de entrada para agregar un Venue a un Partner.
 */
public record AddVenueToPartnerResource(
        String venueName,
        String venueAddress,
        List<CreateTableResource> initialTables
) {
}


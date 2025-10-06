package com.hampcoders.glottia.platform.api.partner.domain.model.queries;

/**
 * Consulta para obtener todas las mesas de un Venue.
 */
public record GetTablesByVenueIdQuery(Long venueId) {
    public GetTablesByVenueIdQuery {
        if (venueId == null) {
            throw new IllegalArgumentException("El venueId no puede ser nulo.");
        }
    }
}


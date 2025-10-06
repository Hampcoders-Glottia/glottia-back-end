package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener todas las mesas disponibles de un Venue.
 */
public record GetAvailableTablesByVenueIdQuery(Long venueId) {
    public GetAvailableTablesByVenueIdQuery {
        if (venueId == null) {
            throw new IllegalArgumentException("El venueId no puede ser nulo.");
        }
    }
}


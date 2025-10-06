package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener un Venue por su ID.
 */
public record GetVenueByIdQuery(Long venueId) {
    public GetVenueByIdQuery {
        if (venueId == null) {
            throw new IllegalArgumentException("El venueId no puede ser nulo.");
        }
    }
}


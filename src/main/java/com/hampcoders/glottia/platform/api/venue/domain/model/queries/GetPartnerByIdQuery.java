package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener un Partner por su ID.
 */
public record GetPartnerByIdQuery(Long partnerId) {
    public GetPartnerByIdQuery {
        if (partnerId == null) {
            throw new IllegalArgumentException("El partnerId no puede ser nulo.");
        }
    }
}


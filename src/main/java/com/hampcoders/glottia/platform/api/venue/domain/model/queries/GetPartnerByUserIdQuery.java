package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener un Partner por el User ID.
 */
public record GetPartnerByUserIdQuery(Long userId) {
    public GetPartnerByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("El userId no puede ser nulo.");
        }
    }
}


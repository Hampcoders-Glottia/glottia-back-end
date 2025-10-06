package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener todos los Venues de un Partner.
 */
public record GetVenuesByPartnerIdQuery(Long partnerId) {
    public GetVenuesByPartnerIdQuery {
        if (partnerId == null) {
            throw new IllegalArgumentException("El partnerId no puede ser nulo.");
        }
    }
}


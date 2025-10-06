package com.hampcoders.glottia.platform.api.venue.domain.model.queries;

/**
 * Consulta para obtener una mesa por su ID.
 */
public record GetTableByIdQuery(Long tableId) {
    public GetTableByIdQuery {
        if (tableId == null) {
            throw new IllegalArgumentException("El tableId no puede ser nulo.");
        }
    }
}


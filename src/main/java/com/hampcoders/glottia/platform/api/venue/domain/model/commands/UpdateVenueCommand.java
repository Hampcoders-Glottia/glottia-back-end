package com.hampcoders.glottia.platform.api.venue.domain.model.commands;

/**
 * Comando mejorado para actualizar un Venue.
 * Incluye el partnerId para mantener la consistencia del agregado.
 */

public record UpdateVenueCommand(
        Long partnerId,
        Long venueId,
        String venueName,
        String venueAddress
) {
    public UpdateVenueCommand {
        if (partnerId == null || partnerId <= 0) {
            throw new IllegalArgumentException("El partnerId debe ser un número positivo.");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("El venueId debe ser un número positivo.");
        }
        if (venueName != null && venueName.isBlank()) {
            throw new IllegalArgumentException("El nombre del venue no puede estar vacío.");
        }
        if (venueAddress != null && venueAddress.isBlank()) {
            throw new IllegalArgumentException("La dirección del venue no puede estar vacía.");
        }
    }
}
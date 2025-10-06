package com.hampcoders.glottia.platform.api.partner.domain.model.commands;

/**
 * Comando mejorado para eliminar un Venue.
 * Incluye el partnerId para mantener la consistencia del agregado.
 */
public record DeleteVenueCommand(
        Long partnerId,
        Long venueId
) {
    public DeleteVenueCommand {
        if (partnerId == null || partnerId <= 0) {
            throw new IllegalArgumentException("El partnerId debe ser un número positivo.");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("El venueId debe ser un número positivo.");
        }
    }
}


package com.hampcoders.glottia.platform.api.partner.domain.model.commands;

/**
 * Comando mejorado para agregar una Table a un Venue.
 * Incluye el partnerId para mantener la consistencia del agregado.
 */
public record AddTableToVenueCommand(
        Long partnerId,
        Long venueId,
        Integer tableNumber,
        Integer capacity
) {
    public AddTableToVenueCommand {
        if (partnerId == null || partnerId <= 0) {
            throw new IllegalArgumentException("El partnerId debe ser un número positivo.");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("El venueId debe ser un número positivo.");
        }
        if (tableNumber == null || tableNumber <= 0) {
            throw new IllegalArgumentException("El número de mesa debe ser un número positivo.");
        }
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser un número positivo.");
        }
    }
}


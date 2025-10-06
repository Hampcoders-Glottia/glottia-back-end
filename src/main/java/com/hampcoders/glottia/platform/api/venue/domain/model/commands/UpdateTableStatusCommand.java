package com.hampcoders.glottia.platform.api.venue.domain.model.commands;

/**
 * Comando mejorado para actualizar el estado de una Table.
 * Incluye el partnerId y venueId para mantener la consistencia del agregado.
 */
public record UpdateTableStatusCommand(
        Long partnerId,
        Long venueId,
        Long tableId,
        String newStatus
) {
    public UpdateTableStatusCommand {
        if (partnerId == null || partnerId <= 0) {
            throw new IllegalArgumentException("El partnerId debe ser un número positivo.");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("El venueId debe ser un número positivo.");
        }
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("El tableId debe ser un número positivo.");
        }
        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo o vacío.");
        }
    }
}


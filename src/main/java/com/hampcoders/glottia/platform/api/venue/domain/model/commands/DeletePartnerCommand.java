package com.hampcoders.glottia.platform.api.venue.domain.model.commands;

/**
 * Comando para eliminar un Partner.
 */
public record DeletePartnerCommand(Long partnerId) {
    public DeletePartnerCommand {
        if (partnerId == null) {
            throw new IllegalArgumentException("El partnerId no puede ser nulo.");
        }
    }
}


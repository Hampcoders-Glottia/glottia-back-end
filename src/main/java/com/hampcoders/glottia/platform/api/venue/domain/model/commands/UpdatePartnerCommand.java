package com.hampcoders.glottia.platform.api.partner.domain.model.commands;

/**
 * Comando para actualizar la información de un Partner.
 */
public record UpdatePartnerCommand(
        Long partnerId,
        String legalName
) {
    public UpdatePartnerCommand {
        if (partnerId == null) {
            throw new IllegalArgumentException("El partnerId no puede ser nulo.");
        }
        if (legalName == null || legalName.isBlank()) {
            throw new IllegalArgumentException("El nombre legal no puede ser nulo o vacío.");
        }
    }
}


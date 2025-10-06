package com.hampcoders.glottia.platform.api.venue.domain.model.commands;

import java.util.List;

import com.hampcoders.glottia.platform.api.venue.domain.model.entities.Venue;

/**
 * Comando para crear un nuevo Partner.
 */
public record CreatePartnerCommand(
        Long userId,
        String legalName,
        List<Venue> initialVenues
) {
    public CreatePartnerCommand {
        if (userId == null) {
            throw new IllegalArgumentException("El userId no puede ser nulo.");
        }
        if (legalName == null || legalName.isBlank()) {
            throw new IllegalArgumentException("El nombre legal no puede ser nulo o vacío.");
        }
    }
}


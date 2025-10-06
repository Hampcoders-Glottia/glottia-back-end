package com.hampcoders.glottia.platform.api.partner.domain.model.commands;

import com.hampcoders.glottia.platform.api.partner.domain.model.entities.TableVenue;

import java.util.List;

/**
 * Comando para agregar un nuevo Venue a un Partner.
 */
public record AddVenueToPartnerCommand(
        Long partnerId,
        String venueName,
        String venueAddress,
        List<TableVenue> initialTables
) {
    public AddVenueToPartnerCommand {
        if (partnerId == null) {
            throw new IllegalArgumentException("El partnerId no puede ser nulo.");
        }
        if (venueName == null || venueName.isBlank()) {
            throw new IllegalArgumentException("El nombre del venue no puede ser nulo o vacío.");
        }
        if (venueAddress == null || venueAddress.isBlank()) {
            throw new IllegalArgumentException("La dirección del venue no puede ser nula o vacía.");
        }
    }
}


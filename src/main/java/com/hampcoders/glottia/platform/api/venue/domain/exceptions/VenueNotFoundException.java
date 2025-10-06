package com.hampcoders.glottia.platform.api.venue.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando no se encuentra un Venue.
 */
public class VenueNotFoundException extends RuntimeException {

    public VenueNotFoundException(Long venueId) {
        super("Venue con ID " + venueId + " no encontrado.");
    }

    public VenueNotFoundException(String message) {
        super(message);
    }
}


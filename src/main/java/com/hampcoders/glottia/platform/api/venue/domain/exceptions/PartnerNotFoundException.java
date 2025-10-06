package com.hampcoders.glottia.platform.api.venue.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando no se encuentra un Partner.
 */
public class PartnerNotFoundException extends RuntimeException {

    public PartnerNotFoundException(Long partnerId) {
        super("Partner con ID " + partnerId + " no encontrado.");
    }

    public PartnerNotFoundException(String message) {
        super(message);
    }
}


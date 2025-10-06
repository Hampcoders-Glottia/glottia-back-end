package com.hampcoders.glottia.platform.api.partner.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta crear un Partner duplicado.
 */
public class DuplicatePartnerException extends RuntimeException {

    public DuplicatePartnerException(Long userId) {
        super("Ya existe un Partner asociado al usuario con ID " + userId + ".");
    }

    public DuplicatePartnerException(String message) {
        super(message);
    }
}


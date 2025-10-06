package com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources;

import java.time.LocalTime;

/**
 * Recurso de respuesta para la información de una Mesa.
 */
public record TableResource(
        Long id,
        String name,
        int capacity,
        Float minimumConsumptionAmount,
        LocalTime startReservationTime,
        LocalTime endReservationTime,
        String status
) {
}
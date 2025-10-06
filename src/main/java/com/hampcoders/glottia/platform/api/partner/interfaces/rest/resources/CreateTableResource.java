package com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources;

import java.time.LocalTime;

/**
 * Recurso de entrada para crear una Table.
 */
public record CreateTableResource(
        String name,
        Integer capacity,
        Float minimumConsumption,
        LocalTime startReservationTime,
        LocalTime endReservationTime
) {
}


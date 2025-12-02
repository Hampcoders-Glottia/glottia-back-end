package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Resource representing an available slot for a venue.
 */
public record AvailableSlotResource(
    Long slotId,
    Long tableId,
    String tableNumber,
    Integer tableCapacity,
    LocalDate availabilityDate,
    LocalTime startHour,
    LocalTime endHour
) {}
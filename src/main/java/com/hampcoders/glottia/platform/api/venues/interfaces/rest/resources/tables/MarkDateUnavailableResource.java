package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;
import java.time.LocalTime;

public record MarkDateUnavailableResource(
        LocalDate date,
        LocalTime startHour,
        LocalTime endHour) {
}

package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReserveTableResource(
        LocalDate reservationDate,
        LocalTime startHour,
        LocalTime endHour) {

}

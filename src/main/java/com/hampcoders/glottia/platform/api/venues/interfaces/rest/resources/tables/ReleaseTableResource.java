package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReleaseTableResource(
        LocalDate releaseDate,
        LocalTime startHour,
        LocalTime endHour) {

}

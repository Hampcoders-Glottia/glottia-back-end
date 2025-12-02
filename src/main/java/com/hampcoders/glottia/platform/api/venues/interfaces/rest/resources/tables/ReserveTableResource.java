package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReserveTableResource(
        @Schema(description = "Reservation date", example = "2025-11-26") LocalDate reservationDate,

        @Schema(description = "Start time of 2-hour slot", example = "15:00:00", type = "string") @JsonFormat(pattern = "HH:mm:ss") LocalTime startHour,

        @Schema(description = "End time of 2-hour slot", example = "17:00:00", type = "string") @JsonFormat(pattern = "HH:mm:ss") LocalTime endHour) {

}

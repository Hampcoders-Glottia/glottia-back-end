package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource for creating availability slots
 * Partners use this to pre-create available time slots for booking
 */
public record CreateAvailabilitySlotResource(
        @Schema(description = "Specific date for slot (null for recurring pattern)", example = "2025-11-26", nullable = true) LocalDate availabilityDate,

        @Schema(description = "Day of week for recurring pattern (null for specific date)", example = "MONDAY", nullable = true) DayOfWeek dayOfWeek,

        @Schema(description = "Start time of 2-hour slot", example = "15:00:00", type = "string") @JsonFormat(pattern = "HH:mm:ss") LocalTime startHour,

        @Schema(description = "End time (must be exactly 2 hours after start)", example = "17:00:00", type = "string") @JsonFormat(pattern = "HH:mm:ss") LocalTime endHour) {
}

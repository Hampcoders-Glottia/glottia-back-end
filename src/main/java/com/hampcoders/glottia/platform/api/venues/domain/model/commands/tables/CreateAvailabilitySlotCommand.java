package com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to create an availability slot for a table
 * Supports both specific date slots and recurring pattern slots
 */
public record CreateAvailabilitySlotCommand(
        Long tableId,
        LocalDate availabilityDate, // Optional - null for recurring
        DayOfWeek dayOfWeek, // Optional - for recurring patterns
        LocalTime startHour,
        LocalTime endHour) {
    public CreateAvailabilitySlotCommand {
        if (tableId == null || tableId <= 0) {
            throw new IllegalArgumentException("Table ID must be provided");
        }
        if (startHour == null || endHour == null) {
            throw new IllegalArgumentException("Start and end times must be provided");
        }

        // Validate 2-hour duration
        long duration = Duration.between(startHour, endHour).toMinutes();
        if (duration != 120) {
            throw new IllegalArgumentException("Slot duration must be exactly 2 hours (120 minutes)");
        }

        // Validate pattern type - must have either date or day of week
        if (availabilityDate == null && dayOfWeek == null) {
            throw new IllegalArgumentException("Either availabilityDate or dayOfWeek must be provided");
        }

        // Cannot have both
        if (availabilityDate != null && dayOfWeek != null) {
            throw new IllegalArgumentException("Cannot specify both availabilityDate and dayOfWeek");
        }
    }
}

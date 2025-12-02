package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

/**
 * Resource representation of an AvailabilityCalendar
 * Supports both specific date slots and recurring weekly patterns
 */
public record AvailabilityCalendarResource(
        Long id,
        Long tableId,
        String availabilityDate, // Nullable - for recurring patterns
        String dayOfWeek, // Nullable - for specific dates (e.g., "MONDAY")
        String startHour, // Format: "HH:mm" (e.g., "12:00")
        String endHour, // Format: "HH:mm" (e.g., "14:00")
        boolean isAvailable,
        boolean isReserved,
        Long encounterId,
        String slotType, // "SPECIFIC_DATE" or "RECURRING_PATTERN"
        Integer durationMinutes // Calculated field - always 120 for 2-hour slots
) {
}

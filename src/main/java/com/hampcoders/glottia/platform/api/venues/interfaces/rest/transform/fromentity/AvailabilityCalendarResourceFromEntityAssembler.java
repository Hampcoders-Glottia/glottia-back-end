package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AvailabilityCalendarResource;

public class AvailabilityCalendarResourceFromEntityAssembler {
    public static AvailabilityCalendarResource toResourceFromEntity(AvailabilityCalendar entity) {
        return new AvailabilityCalendarResource(
                entity.getId(),
                entity.getTable().getId(),
                entity.getAvailabilityDate() != null ? entity.getAvailabilityDate().toString() : null,
                entity.getDayOfWeek() != null ? entity.getDayOfWeek().toString() : null,
                entity.getStartHour().toString(), // Format: "HH:mm:ss"
                entity.getEndHour().toString(), // Format: "HH:mm:ss"
                entity.getIsAvailable(),
                entity.isReserved(),
                entity.getEncounterId() != null ? entity.getEncounterId().encounterId() : null,
                entity.isSpecificDate() ? "SPECIFIC_DATE" : "RECURRING_PATTERN",
                (int) entity.getTimeSlotDuration().toMinutes());
    }
}

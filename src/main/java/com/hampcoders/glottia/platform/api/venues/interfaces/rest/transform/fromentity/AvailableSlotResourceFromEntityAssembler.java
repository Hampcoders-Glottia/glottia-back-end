package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AvailableSlotResource;

/**
 * Assembler to transform AvailabilityCalendar entity to AvailableSlotResource.
 */
public class AvailableSlotResourceFromEntityAssembler {

    public static AvailableSlotResource toResourceFromEntity(AvailabilityCalendar calendar) {
        return new AvailableSlotResource(
            calendar.getId(),
            calendar.getTable().getId(),
            calendar.getTable().getTableNumber(),
            calendar.getTable().getCapacity(),
            calendar.getAvailabilityDate(),
            calendar.getStartHour(),
            calendar.getEndHour()
        );
    }
}
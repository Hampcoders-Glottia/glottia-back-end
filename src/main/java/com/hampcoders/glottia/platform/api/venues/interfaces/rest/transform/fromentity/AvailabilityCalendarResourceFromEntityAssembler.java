package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AvailabilityCalendarResource;

public class AvailabilityCalendarResourceFromEntityAssembler {
    public static AvailabilityCalendarResource toResourceFromEntity(AvailabilityCalendar entity ) {
        return new AvailabilityCalendarResource(
            entity.getId(),
            entity.getTable().getId(),
            entity.getAvailabilityDate().toString(),
            entity.getIsAvailable(),
            entity.isReserved(),
            entity.getEncounterId().encounterId()
        );
    }    
}

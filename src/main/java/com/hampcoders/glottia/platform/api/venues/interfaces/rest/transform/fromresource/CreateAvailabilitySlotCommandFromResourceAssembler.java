package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.CreateAvailabilitySlotCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.CreateAvailabilitySlotResource;

/**
 * Assembler to transform CreateAvailabilitySlotResource to
 * CreateAvailabilitySlotCommand
 */
public class CreateAvailabilitySlotCommandFromResourceAssembler {

    public static CreateAvailabilitySlotCommand toCommandFromResource(
            Long tableId,
            CreateAvailabilitySlotResource resource) {
        return new CreateAvailabilitySlotCommand(
                tableId,
                resource.availabilityDate(),
                resource.dayOfWeek(),
                resource.startHour(),
                resource.endHour());
    }
}

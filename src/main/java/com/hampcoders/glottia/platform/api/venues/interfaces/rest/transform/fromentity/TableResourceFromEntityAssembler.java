package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.TableResource;

public class TableResourceFromEntityAssembler {
    public static TableResource toResourceFromEntity(Table entity) {
        var availabilityCalendarResources = entity.getAvailabilityCalendars().stream()
            .map(AvailabilityCalendarResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        return new TableResource(
            entity.getId(),
            entity.getTableRegistry().getId(),
            entity.getTableNumber(),
            entity.getTableType().getStringTypeName(),
            entity.getTableStatus().getStringStatusName(),
            entity.getCapacity(),
            availabilityCalendarResources
        );
    }
}

package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.EncounterResource;

import java.util.stream.Collectors;

public class EncounterResourceFromEntityAssembler {
    public static EncounterResource toResourceFromEntity(Encounter entity) {
        var attendances = entity.getAttendances().getItems().stream()
                .map(AttendanceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return new EncounterResource(
            entity.getId(),
            entity.getCreatorId().learnerId(),
            entity.getVenueId().venueId(),
            entity.getTableId().tableId(),
            entity.getTopic(),
            entity.getLanguage().getStringName(),
            entity.getLevel().getStringName(),
            entity.getScheduledAt(),
            entity.getStatus().getStringName(),
            entity.getMinCapacity(),
            entity.getMaxCapacity(),
            attendances
        );
    }
}
package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.AttendanceResource;

public class AttendanceResourceFromEntityAssembler {
    public static AttendanceResource toResourceFromEntity(Attendance entity) {
        return new AttendanceResource(
            entity.getId(),
            entity.getLearnerId().learnerId(),
            entity.getStatus().getStringName(),
            entity.getReservedAt(),
            entity.getCheckedInAt(),
            entity.getPointsAwarded(),
            entity.getPointsPenalized()
        );
    }
}
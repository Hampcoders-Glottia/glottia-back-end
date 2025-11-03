package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

public record EncounterResource(
    Long id,
    Long creatorId,
    Long venueId,
    Long tableId,
    String topic,
    String language,
    String level,
    LocalDateTime scheduledAt,
    String status,
    int minCapacity,
    int maxCapacity,
    List<AttendanceResource> attendances
) {}

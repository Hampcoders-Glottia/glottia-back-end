package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AttendanceResource(
    Long id,
    Long learnerId,
    String status,
    LocalDateTime reservedAt,
    LocalDateTime checkedInAt,
    Integer pointsAwarded,
    Integer pointsPenalized
) {}
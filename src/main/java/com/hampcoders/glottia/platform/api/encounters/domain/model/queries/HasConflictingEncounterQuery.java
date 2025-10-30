package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDateTime;

public record HasConflictingEncounterQuery(
    LearnerId learnerId, 
    LocalDateTime scheduledAt) {} // Verifica +/- 2 horas

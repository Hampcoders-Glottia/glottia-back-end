package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;
import java.time.LocalDate;

public record SearchEncountersQuery(
    String location, 
    Language language,
    CEFRLevel level,
    LocalDate date,
    Integer page,
    Integer size) {}

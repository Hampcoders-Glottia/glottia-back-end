package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import java.time.LocalDate;

public record SearchEncountersQuery(
    String location, 
    Long languageId,
    Long cefrlevelId,
    LocalDate date,
    Integer page,
    Integer size) {}

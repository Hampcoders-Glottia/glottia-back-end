package com.hampcoders.glottia.platform.api.encounters.domain.model.queries;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record GetEncountersByCreatorIdAndEncounterStatusIdQuery(
    LearnerId creatorId, 
    Long encounterStatusId) {}
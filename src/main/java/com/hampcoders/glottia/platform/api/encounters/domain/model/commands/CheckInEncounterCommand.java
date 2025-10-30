package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record CheckInEncounterCommand(
    Long encounterId, 
    LearnerId learnerId 
    /* , String qrCode - Post MVP */
    ) {

    }
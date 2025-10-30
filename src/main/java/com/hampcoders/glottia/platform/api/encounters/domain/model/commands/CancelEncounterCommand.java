package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record CancelEncounterCommand(
    Long encounterId, 
    LearnerId cancellerId, 
    String reason) {

        // Incluir quién cancela y por qué

}

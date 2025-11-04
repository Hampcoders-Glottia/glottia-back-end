package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CancelEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CancelEncounterResource;

public class CancelEncounterCommandFromResourceAssembler {
    public static CancelEncounterCommand toCommandFromResource(Long encounterId, LearnerId cancellerId, CancelEncounterResource resource) {
        return new CancelEncounterCommand(
            encounterId,
            cancellerId, // Asumimos que este ID viene del contexto de seguridad
            resource.reason()
        );
    }
}
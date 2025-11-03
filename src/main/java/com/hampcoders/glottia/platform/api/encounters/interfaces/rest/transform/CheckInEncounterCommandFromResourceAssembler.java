package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CheckInEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CheckInEncounterResource;

public class CheckInEncounterCommandFromResourceAssembler {
     public static CheckInEncounterCommand toCommandFromResource(Long encounterId, CheckInEncounterResource resource) {
        return new CheckInEncounterCommand(
            encounterId,
            new LearnerId(resource.learnerId())
        );
    }
}

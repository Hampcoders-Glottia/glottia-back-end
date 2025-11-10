package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.JoinEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.JoinEncounterResource;

public class JoinEncounterCommandFromResourceAssembler {
    public static JoinEncounterCommand toCommandFromResource(Long encounterId, JoinEncounterResource resource) {
        return new JoinEncounterCommand(
            encounterId,
            new LearnerId(resource.learnerId())
        );
    }
}

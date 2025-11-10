package com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.VenueId;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CreateEncounterResource;

public class CreateEncounterCommandFromResourceAssembler {
    public static CreateEncounterCommand toCommandFromResource(CreateEncounterResource resource) {
        return new CreateEncounterCommand(
            new LearnerId(resource.creatorId()),
            new VenueId(resource.venueId()),
            resource.topic(),
            resource.language(),
            resource.cefrLevel(),
            resource.scheduledAt()
        );
    }
}

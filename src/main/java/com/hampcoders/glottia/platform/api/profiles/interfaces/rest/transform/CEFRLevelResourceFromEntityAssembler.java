package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CEFRLevelResource;

public class CEFRLevelResourceFromEntityAssembler {

    public static CEFRLevelResource toResource(CEFRLevel entity) {
        return new CEFRLevelResource(
            entity.getId(),
            entity.getLevel().name()
        );
    }
}

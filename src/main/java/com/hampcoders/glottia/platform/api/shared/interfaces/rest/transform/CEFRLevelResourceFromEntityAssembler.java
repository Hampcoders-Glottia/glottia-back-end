package com.hampcoders.glottia.platform.api.shared.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.CEFRLevelResource;

public class CEFRLevelResourceFromEntityAssembler {

    public static CEFRLevelResource toResource(CEFRLevel entity) {
        return new CEFRLevelResource(
            entity.getId(),
            entity.getName().name()
        );
    }
}

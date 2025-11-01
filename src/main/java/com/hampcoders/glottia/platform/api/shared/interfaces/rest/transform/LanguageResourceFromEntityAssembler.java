package com.hampcoders.glottia.platform.api.shared.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.LanguageResource;

public class LanguageResourceFromEntityAssembler {

    public static LanguageResource toResource(Language entity) {
        return new LanguageResource(
            entity.getId(),
            entity.getStringName()
        );
    }
}

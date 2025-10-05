package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.LanguageResource;

public class LanguageResourceFromEntityAssembler {

    public static LanguageResource toResource(Language entity) {
        return new LanguageResource(
            entity.getId(),
            entity.getLanguage().name()
        );
    }
}

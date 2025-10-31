package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.LearnerLanguageItemResource;

public class LearnerLanguageItemResourceFromEntityAssembler {

    public static LearnerLanguageItemResource toResourceFromEntity(LearnerLanguageItem entity) {
        return new LearnerLanguageItemResource(
            entity.getId(),
            entity.getLearner().getId(),
            entity.getLanguage().getLanguage().name(),
            entity.getCefrLevel().getLevel().name(),
            entity.isLearning()
        );
    }
}

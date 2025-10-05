package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

import java.util.List;

public record LearnerResource(
    Long id,
    AddressResource address,
    List<LearnerLanguageItemResource> languages
) {
}
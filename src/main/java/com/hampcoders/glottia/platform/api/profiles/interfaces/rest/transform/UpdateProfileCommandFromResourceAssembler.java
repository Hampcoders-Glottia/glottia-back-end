package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResourceAssembler {
    public static UpdateProfileCommand toCommandFromResource(Long profileId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(
            profileId,
            resource.firstName(),
            resource.lastName(),
            resource.age(),
            resource.email(),
            // Campos opcionales para Learner
            resource.street(),
            resource.number(),
            resource.city(),
            resource.postalCode(),
            resource.country(),
            // Campos opcionales para Partner
            resource.legalName(),
            resource.businessName(),
            resource.taxId(),
            resource.contactEmail(),
            resource.contactPhone(),
            resource.contactPersonName(),
            resource.description()
        );
    }
}
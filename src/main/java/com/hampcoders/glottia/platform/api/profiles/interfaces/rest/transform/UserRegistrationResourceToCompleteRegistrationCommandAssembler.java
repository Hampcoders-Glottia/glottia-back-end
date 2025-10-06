package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CompleteRegistrationCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.UserRegistrationResource;

public class UserRegistrationResourceToCompleteRegistrationCommandAssembler {
    
    public static CompleteRegistrationCommand toCommandFromResource(UserRegistrationResource resource) {
        return new CompleteRegistrationCommand(
            resource.username(),
            resource.password(),
            resource.firstName(),
            resource.lastName(),
            resource.age(),
            resource.email(),
            resource.businessRole(),
            resource.street(),
            resource.number(),
            resource.city(),
            resource.postalCode(),
            resource.country(),
            resource.latitude(),
            resource.longitude(),
            resource.legalName(),
            resource.businessName(),
            resource.taxId(),
            resource.contactEmail(),
            resource.contactPhone(),
            resource.contactPersonName(),
            resource.description(),
            resource.websiteUrl(),
            resource.instagramHandle()
        );
    }
}
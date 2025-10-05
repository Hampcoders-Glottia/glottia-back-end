package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CreatePartnerResource;

public class CreatePartnerCommandFromResourceAssembler {

    public static CreatePartnerCommand toCommandFromResource(CreatePartnerResource resource) {
        return new CreatePartnerCommand(
            resource.profileId(),
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

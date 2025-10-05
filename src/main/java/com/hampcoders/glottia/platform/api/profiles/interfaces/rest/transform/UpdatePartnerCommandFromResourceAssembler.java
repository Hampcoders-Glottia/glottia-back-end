package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.UpdatePartnerResource;

public class UpdatePartnerCommandFromResourceAssembler {

    public static UpdatePartnerContactCommand toCommandFromResource(Long partnerId, UpdatePartnerResource resource) {
        return new UpdatePartnerContactCommand(
            partnerId,
            resource.contactEmail(),
            resource.contactPhone(),
            resource.contactPersonName()
        );
    }
}

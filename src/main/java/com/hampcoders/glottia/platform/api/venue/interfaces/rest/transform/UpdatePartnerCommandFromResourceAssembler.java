package com.hampcoders.glottia.platform.api.venue.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.venue.domain.model.commands.UpdatePartnerCommand;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources.UpdatePartnerResource;

/**
 * Assembler para convertir UpdatePartnerResource a UpdatePartnerCommand.
 */
public class UpdatePartnerCommandFromResourceAssembler {

    private UpdatePartnerCommandFromResourceAssembler() {
        // Constructor privado para clase utilitaria
    }

    public static UpdatePartnerCommand toCommandFromResource(Long partnerId, UpdatePartnerResource resource) {
        return new UpdatePartnerCommand(
                partnerId,
                resource.legalName()
        );
    }
}


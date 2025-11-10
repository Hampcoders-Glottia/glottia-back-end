package com.hampcoders.glottia.platform.api.venues.application.acl;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreatePartnerVenueRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VenuesContextFacadeImpl implements VenuesContextFacade {

    private final PartnerVenueRegistryCommandService partnerVenueRegistryCommandService;

    @Override
    public Long createPartnerVenueRegistry(Long partnerId) {
        var command = new CreatePartnerVenueRegistryCommand(new PartnerId(partnerId));
        return partnerVenueRegistryCommandService.handle(command); 
    }    
}

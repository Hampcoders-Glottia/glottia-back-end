package com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

@Service
public class ExternalVenueService {
    private final VenuesContextFacade venuesContextFacade;

    public ExternalVenueService(VenuesContextFacade venuesContextFacade) {
        this.venuesContextFacade = venuesContextFacade;
    }

    

}

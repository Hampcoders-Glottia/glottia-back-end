package com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

@Service
public class ExternalVenueService {
    private final VenuesContextFacade venuesContextFacade;

    public ExternalVenueService(VenuesContextFacade venuesContextFacade) {
        this.venuesContextFacade = venuesContextFacade;
    }

    /**
     * Creates a venue registry for the given partner if it does not already exist.
     * @param partnerId The partner ID.
     */
    public void createVenueRegistryForPartner(Long partnerId) {
        if (!venuesContextFacade.exitsVenueRegistryByPartnerId(partnerId)) {
            venuesContextFacade.createPartnerVenueRegistry(partnerId);
        }
    } 

}

package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

import java.util.List;

public record PartnerVenueRegistryResource(Long partnerVenueRegistryId, Long partnerId, List<VenueRegistrationResource> venuesRegistrations) {

}

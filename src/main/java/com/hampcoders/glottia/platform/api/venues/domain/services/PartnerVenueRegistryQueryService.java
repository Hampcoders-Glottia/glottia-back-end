package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetActiveVenueRegistrationsByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAllVenueRegistrationsByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetPartnerRegistriesWithActiveVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetPartnerVenueRegistryByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueCountByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueRegistrationsByPartnerIdAndStatusQuery;

public interface PartnerVenueRegistryQueryService {
    Optional<PartnerVenueRegistry> handle(GetPartnerVenueRegistryByPartnerIdQuery query);
    List<VenueRegistration> handle(GetAllVenueRegistrationsByPartnerIdQuery query);
    List<VenueRegistration> handle(GetActiveVenueRegistrationsByPartnerIdQuery query);
    List<VenueRegistration> handle(GetVenueRegistrationsByPartnerIdAndStatusQuery query);
    List<PartnerVenueRegistry> handle(GetPartnerRegistriesWithActiveVenuesQuery query);
    int handle(GetVenueCountByPartnerIdQuery query);
    /*List<Venue> handle(GetVenuesByPartnerIdQuery query);
    List<Venue> handle(GetActiveVenuesByPartnerIdQuery query);*/
    
}
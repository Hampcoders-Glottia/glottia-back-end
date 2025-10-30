package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllVenueTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetVenueTypeByNameQuery;


public interface VenueTypeQueryService {
    List<VenueType> handle(GetAllVenueTypesQuery query);
    Optional<VenueType> handle(GetVenueTypeByNameQuery query);
}

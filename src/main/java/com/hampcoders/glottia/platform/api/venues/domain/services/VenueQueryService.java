package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetActiveVenuesByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetActiveVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAllVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAvailableSlotsForVenueQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetTotalVenuesCountQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByCityQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByVenueTypeIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesWithActivePromotionsQuery;

public interface VenueQueryService {
    Optional<Venue> handle(GetVenueByIdQuery query);
    Optional<Venue> handle(GetVenueByNameQuery query);
    List<Venue> handle(GetVenuesByCityQuery query);
    List<Venue> handle(GetVenuesByVenueTypeIdQuery query);
    List<Venue> handle(GetActiveVenuesQuery query);
    List<Venue> handle(GetAllVenuesQuery query);
    List<Venue> handle(GetVenuesByPartnerIdQuery query);
    List<Venue> handle(GetActiveVenuesByPartnerIdQuery query);
    List<Venue> handle(GetVenuesWithActivePromotionsQuery query);
    int handle(GetTotalVenuesCountQuery query);
    List<AvailabilityCalendar> handle(GetAvailableSlotsForVenueQuery query);
}
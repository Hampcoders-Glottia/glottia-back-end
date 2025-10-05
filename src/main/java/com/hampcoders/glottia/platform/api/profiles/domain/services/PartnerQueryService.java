package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllPartnersQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByProfileIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnersBySubscriptionStatusQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service for Partner entity operations
 */
public interface PartnerQueryService {
    List<Partner> handle(GetAllPartnersQuery query);
    Optional<Partner> handle(GetPartnerByIdQuery query);
    Optional<Partner> handle(GetPartnerByProfileIdQuery query);
    List<Partner> handle(GetPartnersBySubscriptionStatusQuery query);
}
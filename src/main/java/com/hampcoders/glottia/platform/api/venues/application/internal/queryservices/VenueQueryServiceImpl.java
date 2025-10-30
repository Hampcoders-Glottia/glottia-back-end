package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.*;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueQueryServiceImpl implements VenueQueryService {
    private final VenueRepository venueRepository;

    public VenueQueryServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Optional<Venue> handle(GetVenueByIdQuery query) {
        return venueRepository.findById(query.venueId());
    }

    @Override
    public Optional<Venue> handle(GetVenueByNameQuery query) {
        return venueRepository.findByName(query.name());
    }

    @Override
    public List<Venue> handle(GetVenuesByCityQuery query) {
        return venueRepository.findByAddressCity(query.city());
    }

    @Override
    public List<Venue> handle(GetVenuesByVenueTypeIdQuery query) {
        return venueRepository.findByVenueTypeId(query.venueTypeId());
    }

    @Override
    public List<Venue> handle(GetActiveVenuesQuery query) {
        return venueRepository.findByIsActiveTrue();
    }

    @Override
    public List<Venue> handle(GetAllVenuesQuery query) {
        return venueRepository.findAll();
    }

    @Override
    public int handle(GetTotalVenuesCountQuery query) {
        return (int) venueRepository.count();
    }

    @Override
    public List<Venue> handle(GetVenuesWithActivePromotionsQuery query) {
        return venueRepository.findAllWithActivePromotions();
    }

    @Override
    public List<Venue> handle(GetVenuesByPartnerIdQuery query) {
        return venueRepository.findByPartnerId(query.partnerId());
    }

    @Override
    public List<Venue> handle(GetActiveVenuesByPartnerIdQuery query) {
        return venueRepository.findByPartnerId(query.partnerId()).stream()
                .filter(Venue::isActive)
                .toList();
    }
}
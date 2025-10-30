package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.*;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PartnerVenueRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerVenueRegistryQueryServiceImpl implements PartnerVenueRegistryQueryService {
    private final PartnerVenueRegistryRepository partnerVenueRegistryRepository;
    private final VenueRegistrationRepository venueRegistrationRepository;

    public PartnerVenueRegistryQueryServiceImpl(PartnerVenueRegistryRepository partnerVenueRegistryRepository, VenueRegistrationRepository venueRegistrationRepository) {
        this.partnerVenueRegistryRepository = partnerVenueRegistryRepository;
        this.venueRegistrationRepository = venueRegistrationRepository;
    }

    @Override
    public Optional<PartnerVenueRegistry> handle(GetPartnerVenueRegistryByPartnerIdQuery query) {
        return partnerVenueRegistryRepository.findByPartnerId(query.partnerId());
    }

    /*@Override
    public List<Venue> handle(GetVenuesByPartnerIdQuery query) {
        return partnerVenueRegistryRepository.findByPartnerId(query.partnerId())
                .map(registry -> registry.getVenueList().getRegistrations() // Get the list of VenueRegistration
                        .stream()
                        .map(VenueRegistration::getVenue) // For each registration, get the Venue
                        .collect(Collectors.toList()))   // Collect them into a new list
                .orElse(List.of());
    }

    @Override
    public List<Venue> handle(GetActiveVenuesByPartnerIdQuery query) {
        return partnerVenueRegistryRepository.findByPartnerId(query.partnerId())
                .map(registry -> registry.getVenueList().getActiveRegistrations() // Get active registrations
                        .stream()
                        .map(VenueRegistration::getVenue) // Map to Venue
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }*/

    @Override
    public List<VenueRegistration> handle(GetAllVenueRegistrationsByPartnerIdQuery query) {
        return venueRegistrationRepository.findByPartnerId(query.partnerId());
    }

    @Override
    public List<VenueRegistration> handle(GetActiveVenueRegistrationsByPartnerIdQuery query) {
        return venueRegistrationRepository.findActiveByPartnerId(query.partnerId());
    }

    @Override
    public List<VenueRegistration> handle(GetVenueRegistrationsByPartnerIdAndStatusQuery query) {
        return venueRegistrationRepository.findByPartnerIdAndIsActive(query.partnerId(), query.isActive());
    }

    @Override
    public int handle(GetVenueCountByPartnerIdQuery query) {
        return partnerVenueRegistryRepository.findByPartnerId(query.partnerId())
                .map(registry -> registry.getVenueList().size()) // .size() is correct here
                .orElse(0);
    }

    @Override
    public List<PartnerVenueRegistry> handle(GetPartnerRegistriesWithActiveVenuesQuery query) {
        return partnerVenueRegistryRepository.findAllWithActiveVenues();
    }
}
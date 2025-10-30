package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreatePartnerVenueRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ReactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.AddVenueToPartnerRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PartnerVenueRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRegistrationRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;

@Service
public class PartnerVenueRegistryCommandServiceImpl implements PartnerVenueRegistryCommandService {

    private final PartnerVenueRegistryRepository registryRepository;
    private final VenueRepository venueRepository;
    private final VenueRegistrationRepository registrationRepository;

    public PartnerVenueRegistryCommandServiceImpl(PartnerVenueRegistryRepository registryRepository, 
                                                  VenueRepository venueRepository, 
                                                  VenueRegistrationRepository registrationRepository) {
        this.registryRepository = registryRepository;
        this.venueRepository = venueRepository;
        this.registrationRepository = registrationRepository;
    }
    
    @Override
    public Long handle(CreatePartnerVenueRegistryCommand command) {
        if (registryRepository.existsByPartnerId(command.partnerId())) {
            throw new IllegalArgumentException("PartnerVenueRegistry for PartnerId " + command.partnerId() + " already exists.");
        }
        
        var registry = new PartnerVenueRegistry(command.partnerId());
        registryRepository.save(registry);  
        return registry.getId();
    }

    @Override
    public void handle(AddVenueToPartnerRegistryCommand command) {
        var registry = registryRepository.findByPartnerId(command.partnerId())
            .orElseThrow(() -> new IllegalArgumentException("Partner Venue Registry not found"));
        
        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue not found"));

        if (registrationRepository.existsByPartnerVenueRegistryAndVenue(registry, venue)) {
            throw new IllegalArgumentException("Venue is already registered to this partner.");
        }

        registry.addVenue(venue, command.registrationDate(), command.isActive());
        registryRepository.save(registry);
    }

    @Override
    public void handle(DeactivateVenueRegistrationCommand command) {
        var registry = registryRepository.findById(command.partnerVenueRegistryId())
            .orElseThrow(() -> new IllegalArgumentException("Partner venue registry not found"));
        
        registry.deactivateVenue(command.venueId(), command.reason());
        registryRepository.save(registry);
    }

    @Override
    public void handle(ReactivateVenueRegistrationCommand command) {
        var registry = registryRepository.findById(command.partnerVenueRegistryId())
            .orElseThrow(() -> new IllegalArgumentException("Partner venue registry not found"));

        registry.reactivateVenue(command.venueId());
        registryRepository.save(registry);
    }

}

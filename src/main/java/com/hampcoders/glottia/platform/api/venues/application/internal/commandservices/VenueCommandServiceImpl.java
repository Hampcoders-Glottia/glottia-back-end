package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ActivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.UpdateVenueDetailsCommand;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueTypeRepository;

@Service
public class VenueCommandServiceImpl implements VenueCommandService {

    private final VenueRepository venueRepository;
    private final VenueTypeRepository venueTypeRepository;

    public VenueCommandServiceImpl(VenueRepository venueRepository, VenueTypeRepository venueTypeRepository) {
        this.venueRepository = venueRepository;
        this.venueTypeRepository = venueTypeRepository;
    }

    @Override
    public Long handle(CreateVenueCommand command) {
        if (venueRepository.existsByName(command.name()) && venueRepository.existsByAddress(command.address())) {
            throw new IllegalArgumentException("Venue with the same name and address already exists.");
        }
        
        var venueType = venueTypeRepository.findById(command.venueTypeId())
            .orElseThrow(() -> new IllegalArgumentException("VenueType not found"));
        var venue = new Venue(command,venueType);
        venue = venueRepository.save(venue);
        venue.initializeTableRegistry();
        venueRepository.save(venue);
        return venue.getId();
    }

    @Override
    public Long handle(UpdateVenueDetailsCommand command) {
        if (venueRepository.existsByAddressAndIdIsNot(command.address(), command.venueId())) {
            throw new IllegalArgumentException("Another venue with the same address already exists.");
        }

        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));

        venue.updateDetails(command.address(), command.name(), command.venueTypeId());
        venueRepository.save(venue); 
        return venue.getId();
    }

    @Override
    public void handle(DeactivateVenueCommand command) {
        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));
        
        venue.deactivate();
        venueRepository.save(venue);
    }

    @Override
    public void handle(ActivateVenueCommand command) {
        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));

        venue.activate();
        venueRepository.save(venue);
    }

}

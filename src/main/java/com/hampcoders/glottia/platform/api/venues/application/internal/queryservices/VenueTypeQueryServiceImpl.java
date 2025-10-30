package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllVenueTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetVenueTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueTypeQueryServiceImpl implements VenueTypeQueryService {
    private final VenueTypeRepository venueTypeRepository;

    public VenueTypeQueryServiceImpl(VenueTypeRepository venueTypeRepository) {
        this.venueTypeRepository = venueTypeRepository;
    }

    @Override
    public List<VenueType> handle(GetAllVenueTypesQuery query) {
        return venueTypeRepository.findAll();
    }

    @Override
    public Optional<VenueType> handle(GetVenueTypeByNameQuery query) {
        return venueTypeRepository.findByName(query.name());
    }
}
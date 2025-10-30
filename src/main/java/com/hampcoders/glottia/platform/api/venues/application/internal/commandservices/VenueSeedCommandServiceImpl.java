package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedPromotionTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableStatusesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedVenueTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.PromotionType;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionTypes;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.VenueTypes;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueSeedCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PromotionTypeRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableStatusRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableTypeRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueTypeRepository;

@Service
public class VenueSeedCommandServiceImpl implements VenueSeedCommandService {

    private final VenueTypeRepository venueTypeRepository;
    private final TableTypeRepository tableTypeRepository;
    private final TableStatusRepository tableStatusRepository;
    private final PromotionTypeRepository promotionTypeRepository;

    public VenueSeedCommandServiceImpl(VenueTypeRepository venueTypeRepository, TableTypeRepository tableTypeRepository, TableStatusRepository tableStatusRepository, PromotionTypeRepository promotionTypeRepository) {
        this.venueTypeRepository = venueTypeRepository;
        this.tableTypeRepository = tableTypeRepository;
        this.tableStatusRepository = tableStatusRepository;
        this.promotionTypeRepository = promotionTypeRepository;
    }

    @Override
    public void handle(SeedVenueTypesCommand command) {
        Arrays.stream(VenueTypes.values()).forEach(venueType -> {
            if(!venueTypeRepository.existsByName(venueType)) {
                venueTypeRepository.save(new VenueType(VenueTypes.valueOf(venueType.name())));
            }
        });
    }

    @Override
    public void handle(SeedPromotionTypesCommand command) {
        Arrays.stream(PromotionTypes.values()).forEach(promotionType -> {
            if(!promotionTypeRepository.existsByName(promotionType)) {
                promotionTypeRepository.save(new PromotionType(PromotionTypes.valueOf(promotionType.name())));
            }
        });
    }

    @Override
    public void handle(SeedTableTypesCommand command) {
        Arrays.stream(TableTypes.values()).forEach(tableType -> {
            if(!tableTypeRepository.existsByName(tableType)) {
                tableTypeRepository.save(new TableType(TableTypes.valueOf(tableType.name())));
            }
        });
    }

    @Override
    public void handle(SeedTableStatusesCommand command) {
        Arrays.stream(TableStatuses.values()).forEach(tableStatus -> {
            if(!tableStatusRepository.existsByName(tableStatus)) {
                tableStatusRepository.save(new TableStatus(TableStatuses.valueOf(tableStatus.name())));
            }
        });
    }

}

package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.*;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.AvailabilityCalendarRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableRegistryQueryServiceImpl implements TableRegistryQueryService {
    private final TableRegistryRepository tableRegistryRepository;
    private final TableRepository tableRepository;
    private final AvailabilityCalendarRepository availabilityCalendarRepository;

    public TableRegistryQueryServiceImpl(TableRegistryRepository tableRegistryRepository, TableRepository tableRepository, AvailabilityCalendarRepository availabilityCalendarRepository) {
        this.tableRegistryRepository = tableRegistryRepository;
        this.tableRepository = tableRepository;
        this.availabilityCalendarRepository = availabilityCalendarRepository;
    }

    @Override
    public Optional<Table> handle(GetTableByIdQuery query) {
        return tableRepository.findById(query.tableId());
    }

    @Override
    public List<Table> handle(GetTablesByVenueIdQuery query) {
        return tableRepository.findByVenueId(query.venueId());
    }

    @Override
    public List<Table> handle(GetTablesByTableTypeIdQuery query) {
        return tableRepository.findByTableTypeId(query.tableTypeId());
    }

    @Override
    public List<Table> handle(GetTablesByTableStatusIdQuery query) {
        return tableRepository.findByTableStatusId(query.tableStatusId());
    }

    @Override
    public List<Table> handle(GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery query) {
        return tableRegistryRepository.findByVenueId(query.venueId())
                .map(registry -> registry.getAvailableTables(query.date(), query.minCapacity())) // FIX: Use date() and minCapacity()
                .orElse(List.of());
    }

    @Override
    public List<AvailabilityCalendar> handle(GetTableAvailabilityFromDateToDateQuery query) {
        return availabilityCalendarRepository.findByTableIdAndAvailabilityDateBetween(query.tableId(), query.fromDate(), query.toDate());
    }

    @Override
    public int handle(GetTableCountByVenueIdQuery query) {
        return (int) tableRepository.countByVenueId(query.venueId());
    }

    @Override
    public Optional<TableRegistry> handle(GetTableRegistryByVenueIdQuery query) {
        return tableRegistryRepository.findByVenueId(query.venueId());
    }

    @Override
    public List<TableRegistry> handle(GetTableRegistriesWithAvailableTablesQuery query) {
        return tableRegistryRepository.findAllWithAvailableTablesOnDate(query.date());
    }
}

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

    public TableRegistryQueryServiceImpl(TableRegistryRepository tableRegistryRepository,
            TableRepository tableRepository, AvailabilityCalendarRepository availabilityCalendarRepository) {
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
                .map(registry -> registry.getAvailableTables(query.date(), query.minCapacity()))
                .orElse(List.of());
    }

    @Override
    public List<AvailabilityCalendar> handle(GetTableAvailabilityFromDateToDateQuery query) {
        return availabilityCalendarRepository.findByTableIdAndAvailabilityDateBetween(query.tableId(), query.fromDate(),
                query.toDate());
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

    /**
     * Checks if a specific table has availability at a given time slot.
     * Searches for availability calendars that match the requested time range.
     * 
     * @param query The query containing table ID, date, start time, and end time
     * @return Optional containing the first matching available slot, or empty if
     *         none found
     */
    @Override
    public Optional<AvailabilityCalendar> handle(GetTableAvailabilityForTimeSlotQuery query) {
        var table = tableRepository.findById(query.tableId()).orElse(null);
        if (table == null) {
            return Optional.empty();
        }

        var dayOfWeek = query.date().getDayOfWeek();

        // Get all available slots for the date (both specific date and recurring
        // pattern for the day)
        var slots = availabilityCalendarRepository.findAvailableSlots(table, query.date(), dayOfWeek);

        // Filter for slots that fully contain the requested time range
        return slots.stream()
                .filter(slot -> slot.getIsAvailable() && !slot.isReserved())
                .filter(slot -> !slot.getStartHour().isAfter(query.startTime())
                        && !slot.getEndHour().isBefore(query.endTime()))
                .findFirst();
    }

    /**
     * Finds all available tables at a venue for a specific time.
     * Used for auto-assignment of tables to encounters.
     * 
     * @param query The query containing venue ID, date, and start time
     * @return List of tables that have available slots at the requested time
     */
    @Override
    public List<Table> handle(GetAvailableTablesAtTimeQuery query) {
        // Get all tables for the venue
        var tables = tableRepository.findByVenueId(query.venueId());

        var endTime = query.startTime().plusHours(2); // Encounters are 2 hours
        var dayOfWeek = query.date().getDayOfWeek();

        // Filter tables that have available slots at the requested time
        return tables.stream()
                .filter(table -> {
                    var slots = availabilityCalendarRepository.findAvailableSlots(
                            table,
                            query.date(),
                            dayOfWeek);

                    return slots.stream()
                            .anyMatch(slot -> slot.getIsAvailable()
                                    && !slot.isReserved()
                                    && !slot.getStartHour().isAfter(query.startTime())
                                    && !slot.getEndHour().isBefore(endTime));
                })
                .toList();
    }
}

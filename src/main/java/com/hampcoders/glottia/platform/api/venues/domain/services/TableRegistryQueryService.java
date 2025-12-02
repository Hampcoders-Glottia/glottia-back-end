package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetAvailableTablesAtTimeQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableAvailabilityForTimeSlotQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableAvailabilityFromDateToDateQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableCountByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableRegistriesWithAvailableTablesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableRegistryByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByTableStatusIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByTableTypeIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByVenueIdQuery;

/**
 * Query service interface for table registries.
 * Provides methods to retrieve table and availability information.
 */
public interface TableRegistryQueryService {
    Optional<Table> handle(GetTableByIdQuery query);

    Optional<TableRegistry> handle(GetTableRegistryByVenueIdQuery query);

    List<Table> handle(GetTablesByVenueIdQuery query);

    List<Table> handle(GetTablesByTableTypeIdQuery query);

    List<Table> handle(GetTablesByTableStatusIdQuery query);

    List<Table> handle(GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery query);

    List<AvailabilityCalendar> handle(GetTableAvailabilityFromDateToDateQuery query);

    List<TableRegistry> handle(GetTableRegistriesWithAvailableTablesQuery query);

    int handle(GetTableCountByVenueIdQuery query);

    /**
     * Checks if a specific table has availability at a given time slot.
     * Used for encounter scheduling validation.
     * 
     * @param query The query containing table ID, date, and time range
     * @return Optional containing the availability calendar if slot is available
     */
    Optional<AvailabilityCalendar> handle(GetTableAvailabilityForTimeSlotQuery query);

    /**
     * Finds all available tables at a venue for a specific time.
     * Used for auto-assignment of tables to encounters.
     * 
     * @param query The query containing venue ID, date, and start time
     * @return List of available tables
     */
    List<Table> handle(GetAvailableTablesAtTimeQuery query);
}
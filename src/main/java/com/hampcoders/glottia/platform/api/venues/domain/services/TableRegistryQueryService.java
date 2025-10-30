package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableAvailabilityFromDateToDateQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableCountByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableRegistriesWithAvailableTablesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableRegistryByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByTableStatusIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByTableTypeIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByVenueIdQuery;

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
}
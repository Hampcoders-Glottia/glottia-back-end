package com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables;

import java.time.LocalDate;

public record GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery(Long venueId, LocalDate date, int minCapacity) {

}

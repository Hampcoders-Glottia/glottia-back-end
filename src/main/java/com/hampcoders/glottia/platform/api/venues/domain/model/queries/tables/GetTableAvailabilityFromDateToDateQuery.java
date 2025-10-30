package com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables;

import java.time.LocalDate;

public record GetTableAvailabilityFromDateToDateQuery(Long tableId, LocalDate fromDate, LocalDate toDate) {

}

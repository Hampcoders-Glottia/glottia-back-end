package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

public record AvailabilityCalendarResource(Long id, Long tableId, String availabilityDate, boolean isActive, boolean isReserved, Long encounterId ) {

}

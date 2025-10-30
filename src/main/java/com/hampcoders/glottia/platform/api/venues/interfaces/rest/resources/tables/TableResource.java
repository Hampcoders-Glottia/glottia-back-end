package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.util.List;

public record TableResource(Long id, Long tableRegistryId, String tableNumber, String tableType, String tableStatus, Integer capacity, List<AvailabilityCalendarResource> availabilityCalendars) {
}

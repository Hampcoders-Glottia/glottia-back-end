package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

import java.time.LocalDateTime;

public record AddVenueToPartnerRegistryResource(Long venueId, LocalDateTime registrationDate, Boolean isActive) {
}
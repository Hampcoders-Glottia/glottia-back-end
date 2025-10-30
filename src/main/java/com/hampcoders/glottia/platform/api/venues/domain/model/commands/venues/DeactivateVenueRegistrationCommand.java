package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

/**
 * Command to deactivate a venue registration
 */
public record DeactivateVenueRegistrationCommand(
    Long partnerVenueRegistryId,
    Long venueId,
    String reason
) {
    public DeactivateVenueRegistrationCommand {
        if (partnerVenueRegistryId == null || partnerVenueRegistryId <= 0) {
            throw new IllegalArgumentException("Registry ID must be positive");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be positive");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be null or empty");
        }
    }
}



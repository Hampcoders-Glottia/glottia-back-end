package com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues;

/** 
 * Command to reactivate a venue registration
 */
public record ReactivateVenueRegistrationCommand(
    Long partnerVenueRegistryId,
    Long venueId
) {
    public ReactivateVenueRegistrationCommand {
        if (partnerVenueRegistryId == null || partnerVenueRegistryId <= 0) {
            throw new IllegalArgumentException("Registry ID must be positive");
        }
        if (venueId == null || venueId <= 0) {
            throw new IllegalArgumentException("Venue ID must be positive");
        }
    }
}

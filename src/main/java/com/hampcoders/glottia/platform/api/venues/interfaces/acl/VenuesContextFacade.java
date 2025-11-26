package com.hampcoders.glottia.platform.api.venues.interfaces.acl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Context Facade for Venues module.
 */
public interface VenuesContextFacade {

    /**
     * Creates a partner venue registry.
     * 
     * @param partnerId The partner ID.
     * @return The created venue registry ID.
     */
    Long createPartnerVenueRegistry(Long partnerId);

    /**
     * Fetches the venue registry ID by partner ID.
     * 
     * @param partnerId The partner ID.
     * @return Optional containing venue registry ID if found, empty otherwise.
     */
    boolean exitsVenueRegistryByPartnerId(Long partnerId);

    /**
     * Checks if a venue exists and is active.
     * 
     * @param venueId The venue ID.
     * @return true if venue exists and is active, false otherwise.
     */
    boolean isVenueActive(Long venueId);

    /**
     * Checks if a table exists and is available.
     * 
     * @param tableId The table ID.
     * @param date    The date to check.
     * @return true if table is available, false otherwise.
     */
    boolean isTableAvailable(Long tableId, LocalDate date);

    /**
     * Fetches the venue ID that owns a table.
     * 
     * @param tableId The table ID.
     * @return The venue ID, or 0L if not found.
     */
    Long fetchVenueIdByTableId(Long tableId);

    /**
     * Checks if a venue belongs to a specific partner.
     * 
     * @param venueId   The venue ID.
     * @param partnerId The partner ID.
     * @return true if venue belongs to partner, false otherwise.
     */
    boolean venueBelongsToPartner(Long venueId, Long partnerId);

    /**
     * Fetches table capacity.
     * 
     * @param tableId The table ID.
     * @return The capacity, or 0 if not found.
     */
    int fetchTableCapacity(Long tableId);

    /**
     * Fetches venue name.
     * 
     * @param venueId The venue ID.
     * @return The venue name, or empty string if not found.
     */
    String fetchVenueName(Long venueId);

    /**
     * Fetches venue address.
     * 
     * @param venueId The venue ID.
     * @return The venue address as string, or empty string if not found.
     */
    String fetchVenueAddress(Long venueId);

    /**
     * Checks if a promotion exists and is active.
     * 
     * @param promotionId The promotion ID.
     * @return true if promotion is active, false otherwise.
     */
    boolean isPromotionActive(Long promotionId);

    /**
     * Checks if a promotion belongs to a specific partner.
     * 
     * @param promotionId The promotion ID.
     * @param partnerId   The partner ID.
     * @return true if promotion belongs to partner, false otherwise.
     */
    boolean promotionBelongsToPartner(Long promotionId, Long partnerId);

    /**
     * Fetches active promotions for a venue.
     * 
     * @param venueId The venue ID.
     * @return List of active promotion IDs.
     */
    List<Long> fetchActivePromotionIdsByVenue(Long venueId);

    /**
     * Checks if a venue promotion can be redeemed.
     * 
     * @param venuePromotionId The venue promotion ID.
     * @return true if can be redeemed, false otherwise.
     */
    boolean canRedeemVenuePromotion(Long venuePromotionId);

    /**
     * Checks if a table has an available slot at a specific date and time.
     * Used by Encounters BC to validate table availability before creating
     * encounters.
     * Encounters are 2 hours long, so this checks if a slot is available from
     * scheduledAt to scheduledAt+2h.
     * 
     * @param tableId     The table ID
     * @param scheduledAt The exact scheduled date and time
     * @return true if an available slot exists at that time, false otherwise
     */
    boolean isTableSlotAvailable(Long tableId, LocalDateTime scheduledAt);

    /**
     * Finds an available table at a venue for a specific date and time.
     * Used by Encounters BC for auto-assignment of tables to encounters.
     * 
     * @param venueId     The venue ID
     * @param scheduledAt The exact scheduled date and time
     * @return The table ID if available, or 0L if none found
     */
    Long findAvailableTableAtTime(Long venueId, LocalDateTime scheduledAt);
}
package com.hampcoders.glottia.platform.api.venues.interfaces.acl;

import java.time.LocalDate;
import java.util.List;

/**
 * Context Facade for Venues module.
 */
public interface VenuesContextFacade {

    Long createPartnerVenueRegistry(Long partnerId);
    
     /**
     * Checks if a venue exists and is active.
     * @param venueId The venue ID.
     * @return true if venue exists and is active, false otherwise.
     */
    boolean isVenueActive(Long venueId);

    /**
     * Checks if a table exists and is available.
     * @param tableId The table ID.
     * @param date The date to check.
     * @return true if table is available, false otherwise.
     */
    boolean isTableAvailable(Long tableId, LocalDate date);

    /**
     * Fetches the venue ID that owns a table.
     * @param tableId The table ID.
     * @return The venue ID, or 0L if not found.
     */
    Long fetchVenueIdByTableId(Long tableId);

    /**
     * Checks if a venue belongs to a specific partner.
     * @param venueId The venue ID.
     * @param partnerId The partner ID.
     * @return true if venue belongs to partner, false otherwise.
     */
    boolean venueBelongsToPartner(Long venueId, Long partnerId);

    /**
     * Fetches table capacity.
     * @param tableId The table ID.
     * @return The capacity, or 0 if not found.
     */
    int fetchTableCapacity(Long tableId);

    /**
     * Fetches venue name.
     * @param venueId The venue ID.
     * @return The venue name, or empty string if not found.
     */
    String fetchVenueName(Long venueId);

    /**
     * Fetches venue address.
     * @param venueId The venue ID.
     * @return The venue address as string, or empty string if not found.
     */
    String fetchVenueAddress(Long venueId);

    /**
     * Checks if a promotion exists and is active.
     * @param promotionId The promotion ID.
     * @return true if promotion is active, false otherwise.
     */
    boolean isPromotionActive(Long promotionId);

    /**
     * Checks if a promotion belongs to a specific partner.
     * @param promotionId The promotion ID.
     * @param partnerId The partner ID.
     * @return true if promotion belongs to partner, false otherwise.
     */
    boolean promotionBelongsToPartner(Long promotionId, Long partnerId);

    /**
     * Fetches active promotions for a venue.
     * @param venueId The venue ID.
     * @return List of active promotion IDs.
     */
    List<Long> fetchActivePromotionIdsByVenue(Long venueId);

    /**
     * Checks if a venue promotion can be redeemed.
     * @param venuePromotionId The venue promotion ID.
     * @return true if can be redeemed, false otherwise.
     */
    boolean canRedeemVenuePromotion(Long venuePromotionId);
}
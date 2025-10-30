package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

/**
 * PromotionList Value Object
 * Embeddable collection that manages VenuePromotion items
 */
@ToString
@Embeddable
@Getter
public class PromotionList {
    @Getter
    @OneToMany(mappedBy = "venue", 
           fetch = FetchType.LAZY, 
           cascade = CascadeType.ALL, 
           orphanRemoval = true)
    private List<VenuePromotion> promotionItems;

    public PromotionList() {
        this.promotionItems = new ArrayList<>();
    }

    /**
     * Adds a new venue promotion to the list
     * @param promotion The promotion entity
     * @param validFrom Start date of the promotion
     * @param validUntil End date of the promotion
     * @param maxRedemptions Maximum number of redemptions
     */
    public void addItem(Venue venue, Promotion promotion,
                       LocalDate validFrom, LocalDate validUntil, Integer maxRedemptions) {
        this.promotionItems.add(
            new VenuePromotion(venue, promotion, validFrom, validUntil, maxRedemptions)
        );
    }

    /**
     * Check if a promotion is already active in the venue
     */
    public boolean hasActivePromotion(Long promotionId) {
        return promotionItems.stream()
            .anyMatch(vp -> vp.getId().equals(promotionId) && vp.getIsActive());
    }

    /**
     * Find VenuePromotion by promotion ID
     */
    public VenuePromotion findById(Long venuePromotionId) {
        return promotionItems.stream()
            .filter(vp -> vp.getId().equals(venuePromotionId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Find VenuePromotions by promotion name
     */
    public List<VenuePromotion> findByPromotionName(String promotionName) {
        return promotionItems.stream()
            .filter(vp -> vp.getPromotion().getName().equalsIgnoreCase(promotionName))
            .toList();
    }
    /**
     * Get only active promotions
     */
    public List<VenuePromotion> getActivePromotions() {
        return promotionItems.stream()
            .filter(VenuePromotion::getIsActive)
            .toList();
    }

    /**
     * Get total count
     */
    public int size() {
        return promotionItems.size();
    }
}

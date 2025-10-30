package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.LocalDate;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import jakarta.persistence.Table;

/**
 * VenuePromotion Entity
 * Intermediate entity linking a VenuePromotionRegistry to a Promotion (catalog)
 * Contains venue-specific promotion data: dates, redemption limits, status
 */
@Entity
@Getter
@Table(name = "venue_promotions")
public class VenuePromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(name = "max_redemptions")
    private Integer maxRedemptions;

    @Column(name = "current_redemptions", nullable = false)
    private Integer currentRedemptions;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "deactivation_reason", length = 500)
    private String deactivationReason;

    // Constructor
    protected VenuePromotion() {
        super();
        this.currentRedemptions = 0;
        this.isActive = true;
    }

    /**
     * Constructor usado por PromotionList.addItem()
     * @param registry El agregado padre
     * @param promotionType El tipo de promoción (catálogo)
     * @param promotion Los datos de la promoción (VO inmutable)
     * @param validFrom Fecha de inicio
     * @param validUntil Fecha de fin
     * @param maxRedemptions Límite de redenciones (null = ilimitado)
     */
    public VenuePromotion(Venue venue,Promotion promotion,
                         LocalDate validFrom, LocalDate validUntil, Integer maxRedemptions) {
        this();
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
        validatePromotion(promotion);
        validateDates(validFrom, validUntil);
        validateMaxRedemptions(maxRedemptions);

        this.venue = venue;
        this.promotion = promotion;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.maxRedemptions = maxRedemptions;
    }


    private void validatePromotion(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
    }

    private void validateDates(LocalDate validFrom, LocalDate validUntil) {
        if (validFrom == null || validUntil == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (validUntil.isBefore(validFrom)) {
            throw new IllegalArgumentException("Valid until date cannot be before valid from date");
        }
    }

    private void validateMaxRedemptions(Integer maxRedemptions) {
        if (maxRedemptions != null && maxRedemptions <= 0) {
            throw new IllegalArgumentException("Max redemptions must be positive");
        }
    }

    // Business methods
    public boolean canBeRedeemed() {
        if (!isActive) {
            return false;
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(validFrom) || today.isAfter(validUntil)) {
            return false;
        }

        if (maxRedemptions != null && currentRedemptions >= maxRedemptions) {
            return false;
        }

        return true;
    }

    public void redeem() {
        if (!canBeRedeemed()) {
            throw new IllegalStateException("Promotion cannot be redeemed at this time");
        }

        this.currentRedemptions++;

        // Auto-deactivate if limit reached
        if (maxRedemptions != null && currentRedemptions >= maxRedemptions) {
            deactivate("Maximum redemptions reached");
        }
    }

    public void activate() {
        if (isActive) {
            throw new IllegalStateException("Promotion is already active");
        }
        if (isExpired()) {
            throw new IllegalStateException("Cannot activate expired promotion");
        }
        this.isActive = true;
        this.deactivationReason = null;
    }

    public void deactivate(String reason) {
        if (!isActive) {
            throw new IllegalStateException("Promotion is already inactive");
        }
        this.isActive = false;
        this.deactivationReason = reason;
    }

    public void updateDates(LocalDate newValidFrom, LocalDate newValidUntil) {
        validateDates(newValidFrom, newValidUntil);
        
        if (newValidFrom.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot set start date in the past");
        }

        this.validFrom = newValidFrom;
        this.validUntil = newValidUntil;
    }

    public void updateMaxRedemptions(Integer newMaxRedemptions) {
        validateMaxRedemptions(newMaxRedemptions);

        if (newMaxRedemptions != null && newMaxRedemptions < currentRedemptions) {
            throw new IllegalArgumentException("New limit cannot be less than current redemptions");
        }

        this.maxRedemptions = newMaxRedemptions;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(validUntil);
    }

    public boolean isNotStarted() {
        return LocalDate.now().isBefore(validFrom);
    }

    public boolean isOngoing() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(validFrom) && !today.isAfter(validUntil);
    }

    public int getRemainingRedemptions() {
        if (maxRedemptions == null) {
            return Integer.MAX_VALUE; // Unlimited
        }
        return Math.max(0, maxRedemptions - currentRedemptions);
    }

    public Venue getVenue() {
        return venue;
    }
}
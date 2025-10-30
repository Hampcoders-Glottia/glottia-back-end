package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.LocalDateTime;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * VenueRegistration Entity
 * Represents the registration fact linking a Partner to a Venue
 * @since 1.0.0
 */
@Entity
@Getter
@Table(name = "venue_registrations")
public class VenueRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "partner_venue_registry_id")
    private PartnerVenueRegistry partnerVenueRegistry;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "deactivation_date")
    private LocalDateTime deactivationDate;

    @Column(name = "deactivation_reason", length = 500)
    private String deactivationReason;

    /**
     * Protected constructor for JPA
     */
    protected VenueRegistration() {
        super();
        this.isActive = true;
    }

    /**
     * Full constructor with all business fields
     * Similar to ComponentStock constructor pattern
     * @param partnerVenueRegistry The parent aggregate
     * @param venue The venue being registered
     * @param registrationDate The date of registration
     * @param isActive Whether the registration is active
     */
    public VenueRegistration(PartnerVenueRegistry partnerVenueRegistry, Venue venue, LocalDateTime registrationDate, Boolean isActive) {
        this();
        if (partnerVenueRegistry == null) {
            throw new IllegalArgumentException("Partner venue registry cannot be null");
        }
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
        if (registrationDate == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
        if (isActive == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }
        
        this.partnerVenueRegistry = partnerVenueRegistry;
        this.venue = venue;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
    }

    /**
     * Convenience constructor with default values (most common use case)
     * Creates an active registration with current timestamp
     * @param partnerVenueRegistry The parent aggregate
     * @param venue The venue being registered
     */
    public VenueRegistration(PartnerVenueRegistry partnerVenueRegistry, Venue venue) {
        this(partnerVenueRegistry, venue, LocalDateTime.now(), true);
    }

    /**
     * Deactivate the registration
     * @param reason
     */
    public void deactivate(String reason) {
        if (!isActive) {
            throw new IllegalStateException("Registration is already inactive");
        }
        this.isActive = false;
        this.deactivationDate = LocalDateTime.now();
        this.deactivationReason = reason;
    }

    /**
     * Reactivate the registration
     */
    public void reactivate() {
        if (isActive) {
            throw new IllegalStateException("Registration is already active");
        }
        this.isActive = true;
        this.deactivationDate = null;
        this.deactivationReason = null;
    }

    public boolean isDeactivated() {
        return !isActive;
    }
}
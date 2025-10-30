package com.hampcoders.glottia.platform.api.venues.domain.model.aggregates;

import java.time.LocalDate;
import java.util.List;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionList;

import io.jsonwebtoken.lang.Strings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;   
import lombok.Getter;

@Entity
@Getter
@Table(name = "venues")
public class Venue extends AuditableAbstractAggregateRoot<Venue> {

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_type_id", nullable = false)
    private VenueType venueType ;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToOne(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TableRegistry tableRegistry;

    @Embedded
    private PromotionList promotionList;

    protected Venue() {
        super();
        this.name = Strings.EMPTY;
        this.address = null;
        this.venueType = null;
        this.isActive = true;
        this.promotionList = new PromotionList();
    }

    public Venue(CreateVenueCommand command, VenueType venueType) {
        this();
        validateName(command.name());
        validateAddress(command.address());
        validateVenueType(venueType);
        this.name = command.name();
        this.address = command.address();
        this.venueType = venueType;
        this.isActive = true;
    }

    /**
     * Adds a promotion to this venue
     * Note: Partner validation is done in the service layer (VenueCommandServiceImpl)
     * @param promotion The promotion entity
     * @param validFrom Start date
     * @param validUntil End date
     * @param maxRedemptions Max redemptions (null = unlimited)
     */
    public void addPromotion(Promotion promotion, LocalDate validFrom, LocalDate validUntil, Integer maxRedemptions) {
        // Validar fechas (sin partner aquí)
        if (validFrom.isAfter(validUntil)) {
            throw new IllegalArgumentException("Valid from date cannot be after valid until date");
        }
        
        if (maxRedemptions != null && maxRedemptions <= 0) {
            throw new IllegalArgumentException("Max redemptions must be positive");
        }
        
        // Validar duplicados (sin partner)
        boolean alreadyExists = promotionList.getPromotionItems().stream()
            .anyMatch(vp -> vp.getPromotion().getId().equals(promotion.getId()) && vp.getIsActive());
        if (alreadyExists) {
            throw new IllegalStateException("Promotion is already active in this venue");
        }
        
        promotionList.addItem(this,promotion, validFrom, validUntil, maxRedemptions);
    }


    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Venue name cannot be null or empty");
        }
    }

    private void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }

    private void validateVenueType(VenueType venueType) {
        if (venueType == null) {
            throw new IllegalArgumentException("Venue type cannot be null");
        }
    }

    public void deactivate() {
        if (Boolean.FALSE.equals(isActive)) {
            throw new IllegalStateException("Venue is already inactive");
        }
        
        this.isActive = false;
    }

    public void activate() {
        if (Boolean.TRUE.equals(isActive)) {
            throw new IllegalStateException("Venue is already active");
        }
        
        this.isActive = true;
    }

    public void updateDetails(Address newAddress, String newName, Long newVenueTypeId) {
        updateAddress(newAddress);
        updateName(newName);
        updateVenueType(VenueType.toVenueTypeFromId(newVenueTypeId));
    }
    
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void updateAddress(Address newAddress) {
        validateAddress(newAddress);
        this.address = newAddress;
    }

    public void updateVenueType(VenueType newType) {
        validateVenueType(newType);
        this.venueType = newType;
    }

    // Getters
    public boolean hasTableRegistry() {
        return this.tableRegistry != null;
    }

    public String getVenueTypeName() {
        return venueType.getStringTypeName();
    }

    public void deactivatePromotion(Long venuePromotionId, String reason) {
        VenuePromotion vp = promotionList.findById(venuePromotionId);
        if (vp == null) {
            throw new IllegalArgumentException(
                "Promotion not found in this venue with ID: " + venuePromotionId);
        }
        vp.deactivate(reason);
    }
    public void activatePromotion(Long venuePromotionId) {
        VenuePromotion vp = promotionList.findById(venuePromotionId);
        if (vp == null) {
            throw new IllegalArgumentException(
                "Promotion not found in this venue with ID: " + venuePromotionId);
        }
        vp.activate();
    }
    public List<VenuePromotion> getActivePromotions() {
        return promotionList.getPromotionItems().stream()
            .filter(VenuePromotion::getIsActive)
            .filter(VenuePromotion::isOngoing)
            .toList();
    }
    public void initializeTableRegistry() {
        if (this.tableRegistry != null) {
            throw new IllegalStateException("TableRegistry already initialized");
        }
        this.tableRegistry = new TableRegistry(this);
    }
}

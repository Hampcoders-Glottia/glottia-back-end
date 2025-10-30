package com.hampcoders.glottia.platform.api.venues.domain.model.aggregates;

import java.time.LocalDateTime;
import java.util.List;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.VenueList;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;


@Entity
@Getter
@Table(name = "partner_venue_registries")
public class PartnerVenueRegistry extends AuditableAbstractAggregateRoot<PartnerVenueRegistry>{

    @Embedded
    private PartnerId partnerId;

    @Embedded
    private final VenueList venueList;

    /**
     * Protected constructor for JPA
     */
    protected PartnerVenueRegistry() {
        this.venueList = new VenueList();
    }

    /**
     * Constructor with PartnerId
     * Creates a new registry for a partner
     * @param partnerId The partner's identifier from IAM/Profiles BC
     */
    public PartnerVenueRegistry(PartnerId partnerId) {
        this();
        validatePartnerId(partnerId);
        this.partnerId = partnerId;
    }

    private void validatePartnerId(PartnerId partnerId) {
        if (partnerId == null) {
            throw new IllegalArgumentException("Partner ID cannot be null");
        }
        if (partnerId.partnerId() == null || partnerId.partnerId() <= 0) {
            throw new IllegalArgumentException("Partner ID must be positive");
        }
    }

    private void validateVenue(Venue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
    }

    /**
     * Registers a new venue for this partner
     * Validates uniqueness and creates active registration
     * 
     * @param venue The venue to register
     * @return The created VenueRegistration
     * @throws IllegalStateException if venue is already registered
     */
    public void addVenue(Venue venue, LocalDateTime registrationDate, Boolean isActive) {
        validateVenue(venue);
        if (isVenueRegistered(venue)) {
            throw new IllegalStateException("Venue is already registered for this partner");
        }
        if (!venue.isActive()) {
            throw new IllegalStateException("Cannot register an inactive venue");
        }

        venueList.addItem(this, venue, registrationDate, isActive);
    }

    /**
     * Deactivates a venue registration
     * Does not delete, maintains historical record
     * 
     * @param venueId The ID of the venue to deactivate
     * @param reason Reason for deactivation
     */
    public void deactivateVenue(Long venueId, String reason) {
        if (!canDeactivateVenue(venueId)) {
            throw new IllegalStateException(
                "Cannot deactivate venue: venue not found, already inactive, or has future encounters"
            );
        }
        
        VenueRegistration registration = findRegistrationByVenueId(venueId);
        registration.deactivate(reason);
    }

    /**
     * Reactivates a venue registration
     * 
     * @param venueId The ID of the venue to reactivate
     */
    public void reactivateVenue(Long venueId) {
        VenueRegistration registration = findRegistrationByVenueId(venueId);
        if (registration == null) {
            throw new IllegalArgumentException("Venue not found in registry");
        }
        
        // Validar que el venue siga activo
        if (!registration.getVenue().isActive()) {
            throw new IllegalStateException("Cannot reactivate registration for an inactive venue");
        }
        
        registration.reactivate();
    }

    /**
     * Gets all venues (active and inactive)
     * 
     * @return List of all VenueRegistrations
     */
    public List<VenueRegistration> getAllVenues() {
        return List.copyOf(venueList.getRegistrations());
    }

    /**
     * Gets only active venue registrations
     * 
     * @return List of active VenueRegistrations
     */
    public List<VenueRegistration> getActiveVenues() {
        return venueList.getActiveRegistrations();
    }

    /**
     * Gets only inactive venue registrations
     * 
     * @return List of inactive VenueRegistrations
     */
    public List<VenueRegistration> getInactiveVenues() {
        return venueList.getInactiveRegistrations();
    }

    /**
     * Checks if a venue is registered (regardless of active status)
     * 
     * @param venueId The venue ID to check
     * @return true if venue is registered
     */
    public boolean isVenueRegistered(Venue venue) {
        return venueList.hasVenue(venue);
    }

    /**
     * Checks if partner has any active venues
     * 
     * @return true if at least one active venue exists
     */
    public boolean hasActiveVenues() {
        return !venueList.getActiveRegistrations().isEmpty();
    }

    /**
     * Gets total count of registered venues
     * 
     * @return Total number of venues (active + inactive)
     */
    public int getTotalVenueCount() {
        return venueList.size();
    }

    /**
     * Gets count of active venues only
     * 
     * @return Number of active venues
     */
    public int getActiveVenueCount() {
        return venueList.getActiveRegistrations().size();
    }

    /**
     * Gets a specific venue registration by venue ID
     * 
     * @param venueId The venue ID to find
     * @return The VenueRegistration or null if not found
     */
    public VenueRegistration getVenueRegistration(Long venueId) {
        return findRegistrationByVenueId(venueId);
    }


    // MORE BUSINESS RULES : EXTRA
    /**
     * Checks if partner can register more venues
     * Can add business rule like max venues per partner here
     * 
     * @return true if can register more venues
     */
    public boolean canRegisterMoreVenues() {
        // TODO: Implement business rule (e.g., max 10 venues per partner)
        // For now, always allow
        return true;
    }

    /**
     * Validates if venue can be deactivated
     * Checks if there are active encounters, reservations, etc.
     * 
     * @param venueId The venue ID to check
     * @return true if can be deactivated
     */
    public boolean canDeactivateVenue(Long venueId) {
        VenueRegistration registration = findRegistrationByVenueId(venueId);
        if (registration == null) {
            return false;
        }
        
        // Already inactive
        if (!registration.getIsActive()) {
            return false;
        }

        // TODO: Check with Encounters BC if venue has future encounters
        // For now, allow deactivation
        return true;
    }

    // ==================== PRIVATE HELPERS ====================

    /**
     * Finds a venue registration by venue ID
     * 
     * @param venueId The venue ID to search
     * @return The VenueRegistration or null
     */
    private VenueRegistration findRegistrationByVenueId(Long venueId) {
        return venueList.getRegistrations().stream()
            .filter(reg -> reg.getVenue().getId().equals(venueId))
            .findFirst()
            .orElse(null);
    }

    
    

}

package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

@ToString
@Embeddable
public class VenueList {

    @Getter
    @OneToMany(mappedBy = "partnerVenueRegistry", fetch = FetchType.LAZY, 
               cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VenueRegistration> registrations;

    public VenueList() {
        this.registrations = new ArrayList<>();
    }

    /**
     * Add a new venue registration with all parameters (full control)
     */
    public void addItem(PartnerVenueRegistry registry, Venue venue, 
                       LocalDateTime registrationDate, Boolean isActive) {
        this.registrations.add(new VenueRegistration(registry, venue, registrationDate, isActive));
    }

    /**
     * Add a new venue registration with defaults (convenience method)
     * Creates an active registration with current timestamp
     */
    public void addItem(PartnerVenueRegistry registry, Venue venue) {
        this.registrations.add(new VenueRegistration(registry, venue));
    }

    /**
     * Add multiple venue registrations at once 
     */
    public void addItems(List<VenueRegistration> registrations) {
        this.registrations.addAll(registrations);
    }

    /**
     * Check if a venue is already registered
     */
    public boolean hasVenue(Venue venue) {
        return registrations.stream()
            .anyMatch(reg -> reg.getVenue().equals(venue));
    }

    /**
     * Get only active registrations
     * @return List of active VenueRegistrations
     */
    public List<VenueRegistration> getActiveRegistrations() {
        return registrations.stream()
            .filter(VenueRegistration::getIsActive)
            .toList();
    }

    /**
     * Get only inactive registrations
     * @return List of inactive VenueRegistrations
     */
    public List<VenueRegistration> getInactiveRegistrations() {
        return registrations.stream()
            .filter(reg -> !reg.getIsActive())
            .toList();
    }

    /**
     * Get total count
     */
    public int size() {
        return registrations.size();
    }

}

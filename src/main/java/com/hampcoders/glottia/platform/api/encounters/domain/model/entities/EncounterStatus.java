package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * EncounterStatus Entity
 * Catalog entity representing the status of an encounter.
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "encounter_statuses")
public class EncounterStatus {
    /**
     * Unique identifier for the encounter status.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the encounter status.
     */
    @Enumerated(EnumType.STRING)
    private EncounterStatuses name;

    /**
     * Constructor with name parameter.
     * @param name
     */
    public EncounterStatus(EncounterStatuses name) {
        this.name = name;
    }

    /**
     * Get the string name of the encounter status.
     * @return EncounterStatuses
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the integer value associated with the encounter status name. 
     * @return int
     */
    public int getNameValue() {
        return name.getValue();
    }

    /**
     * Get the default EncounterStatus entity (DRAFT).
     * @return EncounterStatus instance
     */
    public static EncounterStatus getDefaultEncounterStatus() {
        return new EncounterStatus(EncounterStatuses.DRAFT);
    }

    /**
     * Convert a String name to an EncounterStatus instance.
     * @param name
     * @return EncounterStatus instance
     */
    public static EncounterStatus toEncounterStatusFromName(String name) {
        return new EncounterStatus(EncounterStatuses.valueOf(name));
    }

    /**
     * Convert a Long id to an EncounterStatus instance.
     * @param id
     * @return EncounterStatus instance
     */
    public static EncounterStatus toEncounterStatusFromId(Long id) {
        return new EncounterStatus(EncounterStatuses.fromValue(id.intValue()));
    }

    /**
     * Check if the encounter status is DRAFT.
     * @return true if DRAFT, false otherwise
     */
    public boolean isDraft() {
        return EncounterStatuses.DRAFT.equals(this.name);
    }

    /**
     * Check if the encounter status is PUBLISHED.
     * @return true if PUBLISHED, false otherwise
     */
    public boolean isPublished() {
        return EncounterStatuses.PUBLISHED.equals(this.name);
    }
    
    /**
     * Check if the encounter status is READY.
     * @return true if READY, false otherwise
     */
    public boolean isReady() {
        return EncounterStatuses.READY.equals(this.name);
    }

    /**
     * Check if the encounter status is IN_PROGRESS.
     * @return true if IN_PROGRESS, false otherwise
     */
    public boolean canTransitionTo(EncounterStatuses newStatus) {
        return switch (this.name) {
            case DRAFT -> newStatus == EncounterStatuses.PUBLISHED;
            case PUBLISHED -> newStatus == EncounterStatuses.READY || newStatus == EncounterStatuses.CANCELLED;
            case READY -> newStatus == EncounterStatuses.IN_PROGRESS || newStatus == EncounterStatuses.CANCELLED;
            case IN_PROGRESS -> newStatus == EncounterStatuses.COMPLETED || newStatus == EncounterStatuses.CANCELLED;
            case COMPLETED, CANCELLED -> false;
            default -> false;
        };
    }

}

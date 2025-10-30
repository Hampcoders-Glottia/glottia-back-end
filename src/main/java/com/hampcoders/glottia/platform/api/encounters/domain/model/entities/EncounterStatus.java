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
    public EncounterStatuses getStringName() {
        return name;
    }

    /**
     * Get the integer value associated with the encounter status name. 
     * @return int
     */
    public int getNameValue() {
        return name.getValue();
    }

    /**
     * Create an EncounterStatus from an integer value.
     * @param value
     * @return EncounterStatus
     */
    public static EncounterStatus fromValue(int value) {
        EncounterStatuses statusEnum = EncounterStatuses.fromValue(value);
        return new EncounterStatus(statusEnum);
    }


}

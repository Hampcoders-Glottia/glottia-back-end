package com.hampcoders.glottia.platform.api.venues.domain.model.entities;


import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.VenueTypes;

import jakarta.persistence.Column;
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
 * VenueType Entity
 * Catalog entity that represents a type of venue
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venue_types")
public class VenueType {

    /**
     * Unique identifier of the venue type
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of the venue
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private VenueTypes name;

    /**
     * Constructor with type as parameter
     */
    public VenueType(VenueTypes name) {
        this.name = name;
    }

    /**
     * Get string representation of the type name
     */
    public String getStringTypeName() {
        return name.name();
    }

    /**
     * Get integer value of the type
     */
    public int getTypeValue() {
        return name.getValue();
    }

    /** 
     * Convert from String name to VenueType entity
     * @param name
     */
    public static VenueType toVenueTypeFromName(String name) {
        return new VenueType(VenueTypes.valueOf(name));
    }

    public static VenueType toVenueTypeFromId(Long newVenueTypeId) {
        return new VenueType(VenueTypes.fromValue(newVenueTypeId.intValue()));
    }
}
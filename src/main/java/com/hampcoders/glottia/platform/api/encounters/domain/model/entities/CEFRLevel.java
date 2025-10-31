package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.CEFRLevels;

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
 * CEFRLevel Entity
 * Catalog entity representing a CEFR level.
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cefr_levels")
public class CEFRLevel {

    /**
     * Unique identifier for the CEFRLevel.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the CEFRLevel.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private CEFRLevels name;

    /**
     * Constructor to create CEFRLevel from CEFRLevels enum.
     * @param name The CEFRLevels enum value.
     * @return CEFRLevel instance.
     */
    public CEFRLevel(CEFRLevels name) {
        this.name = name;
    }

    /**
     * Get the string name of the CEFRLevel.
     * @return String name
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the integer value of the CEFRLevel.
     * @return int value
     */
    public int getNameValue() {
        return name.getValue();
    }


    public static CEFRLevel toCefrLevelFromName(String name) {
        return new CEFRLevel(CEFRLevels.valueOf(name));
    }

    /**
     * Convert CEFRLevel from integer value.
     * @param value
     * @return CEFRLevel instance
     */
    public static CEFRLevel toCefrLevelFromId(Long newCEFRLevelId) {
        return new CEFRLevel(CEFRLevels.fromValue(newCEFRLevelId.intValue()));
    }
}

package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionTypes;

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
 * PromotionType Entity
 * Catalog entity that represents a type of promotion
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion_types")
public class PromotionType {

    /**
     * Unique identifier of the promotion type
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of the promotion
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private PromotionTypes name;

    /**
     * Constructor with type as parameter
     */
    public PromotionType(PromotionTypes name) {
        this.name = name;
    }

    /**
     * Get string representation of the type name
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get integer value of the type
     */
    public int getNameValue() {
        return name.getValue();
    }

    /**
     * Static factory method to create PromotionType from string name
     */
    public static PromotionType toPromotionTypeFromName(String name) {
        return new PromotionType(PromotionTypes.valueOf(name));
    }
}

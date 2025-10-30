package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;

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
 * TableType Entity
 * Catalog entity that represents a type of table
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "table_types")
public class TableType {

    /**
     * Unique identifier of the table type
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of the table
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private TableTypes name;

    /**
     * Constructor with type as parameter
     */
    public TableType(TableTypes name) {
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
     * Convert from string name to TableType entity
     * @param name
     */
    public static TableType toTableTypeFromName(String name) {
        return new TableType(TableTypes.valueOf(name));
    }
}
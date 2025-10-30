package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;

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
 * TableStatus Entity
 * Catalog entity that represents a status of a table
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "table_statuses")
public class TableStatus {

    /**
     * Unique identifier of the table status
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Status of the table
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private TableStatuses name;

    /**
     * Constructor with status as parameter
     * @param status
     */
    public TableStatus(TableStatuses name) {
        this.name = name;
    }

    /**
     * Get string representation of the status name
     */
    public String getStringStatusName() {
        return name.name();
    }

    /**
     * Get integer value of the status
     */
    public int getStatusValue() {
        return name.getValue();
    }

    /**
     * Static method to create TableStatus from string name
     */
    public static TableStatus toTableStatusFromName(String name) {
        return new TableStatus(TableStatuses.valueOf(name));
    }
}
package com.hampcoders.glottia.platform.api.venues.domain.model.aggregates;

import java.time.LocalDate;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableList;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import java.util.List;

/**
 * TableRegistry Aggregate Root
 * Manages all tables for a specific venue (1:1 relationship)
 * Similar to TechnicianInventory pattern
 */
@Entity
@Getter
@jakarta.persistence.Table(name = "table_registries")
public class TableRegistry extends AuditableAbstractAggregateRoot<TableRegistry> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false, unique = true)
    private Venue venue;

    @Embedded
    private TableList tableList;

    /**
     * Protected constructor for JPA
     */
    protected TableRegistry() {
        super();
        this.tableList = new TableList();
    }

    /**
     * Constructor that establishes 1:1 relationship with Venue
     * @param venue The venue that owns this registry
     */
    public TableRegistry(Venue venue) {
        this();
        if (venue == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }
        this.venue = venue;
    }

    // Business methods
    /**
     * Adds a new table to the registry
     * Validates unique table number within the venue
     * @param tableNumber Unique identifier for the table
     * @param capacity Number of people the table can accommodate
     * @param tableType Type entity from catalog
     * @param tableStatus Status entity from catalog
     * @return The created Table entity
     */
    public void addTable(String tableNumber, Integer capacity, TableType tableType, TableStatus tableStatus) {
        if (tableList.hasTableNumber(tableNumber)) {
            throw new IllegalStateException("Table number already exists: " + tableNumber);
        }
        
        tableList.addTable(this, tableNumber, capacity, tableType, tableStatus);
    }

    /**
     * Reserve a table for a specific date
     * @param tableId The ID of the table to reserve
     * @param date The date for the reservation
     */
    public void reserveTable(Long tableId, LocalDate date) {
        Table table = findTableById(tableId);
        if (table == null) {
            throw new IllegalArgumentException("Table not found in this registry");
        }
        
        table.reserve(date);
    }

    /**
     * Release a table for a specific date
     * @param tableId The ID of the table to release
     * @param date The date to release
     */
    public void releaseTable(Long tableId, LocalDate date, TableStatus availableStatus) {
        Table table = findTableById(tableId);
        if (table == null) {
            throw new IllegalArgumentException("Table not found in this registry");
        }

        table.release(date, availableStatus);
    }

    /**
     * Remove a table from the registry
     * Only allowed if table has no active reservations
     * @param tableId The ID of the table to remove
     */
    public void removeTable(Long tableId) {
        Table table = findTableById(tableId);
        if (table == null) {
            throw new IllegalArgumentException("Table not found in this registry");
        }
        
        if (!table.canBeDeleted()) {
            throw new IllegalStateException("Cannot delete table with active reservations");
        }
        
        tableList.removeTable(table);
    }

    /**
     * Get available tables for a specific date and minimum capacity
     * @param date The date to check availability
     * @param minCapacity Minimum capacity required
     * @return List of available tables matching criteria
     */
    public List<Table> getAvailableTables(LocalDate date, Integer minCapacity) {
        return tableList.getTables().stream()
            .filter(Table::isAvailable)
            .filter(table -> table.getCapacity() >= minCapacity)
            .toList();
    }

    /**
     * Get total count of tables in this venue
     * @return Total number of tables
     */
    public int getTotalTableCount() {
        return tableList.size();
    }

    /**
     * Get count of available tables
     * @return Number of available tables
     */
    public int getAvailableTableCount() {
        return (int) tableList.getTables().stream()
            .filter(Table::isAvailable)
            .count();
    }

    // Private helpers
    private Table findTableById(Long tableId) {
        return tableList.getTables().stream()
            .filter(table -> table.getId().equals(tableId))
            .findFirst()
            .orElse(null);
    }
}
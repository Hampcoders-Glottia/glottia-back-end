package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Embeddable
public class TableList {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableRegistry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Table> tables;

    public TableList() {
        this.tables = new ArrayList<>();
    }

    /**
     * Adds a new table to the list
     * @param registry The TableRegistry that owns this list
     * @param tableNumber Unique table number within the venue
     * @param capacity Number of people the table can accommodate
     * @param tableType Type of table (ENCOUNTER_TABLE, GENERAL_TABLE)
     * @param tableStatus Initial status (usually AVAILABLE)
     */
    public void addTable(TableRegistry registry, String tableNumber, Integer capacity, TableType tableType, TableStatus tableStatus) {
        this.tables.add(new Table(registry, tableNumber, capacity, tableType, tableStatus));
    }

    /**
     * Remove a table from the list
     */
    public void removeTable(Table table) {
        this.tables.remove(table);
    }

    /**
     * Check if a table number already exists
     */
    public boolean hasTableNumber(String tableNumber) {
        return tables.stream()
            .anyMatch(table -> table.getTableNumber().equals(tableNumber));
    }

    /**
     * Find table by table number
     */
    public Table findByTableNumber(String tableNumber) {
        return tables.stream()
            .filter(table -> table.getTableNumber().equals(tableNumber))
            .findFirst()
            .orElse(null);
    }

    /**
     * Get total count
     */
    public int size() {
        return tables.size();
    }
}

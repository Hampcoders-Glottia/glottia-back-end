package com.hampcoders.glottia.platform.api.venue.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableStatus;

/**
 * Entidad de dominio. Representa un Local (cafetería, bar, etc.).
 * Es parte del agregado Partner.
 */

@Getter
@Setter
@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    // ... más información del local

    // Relación de composición: las mesas existen solo dentro del local.
    // CascadeType.ALL: Si el Venue se borra, las Tables también.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id") // Clave foránea en la tabla venue_tables
    private List<TableVenue> tables = new ArrayList<>();

    // Identificador para relacionar con el Core BC de Event.
    // Este ID puede ser referenciado por otros BCs.
    private String externalVenueId;

    protected Venue() {
    }

    /**
     * Constructor principal. Valida los campos obligatorios.
     */
    public Venue(String name, String address, List<TableVenue> tables) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del local no puede ser nulo o vacío.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede ser nula o vacía.");
        }
        this.name = name;
        this.address = address;
        if (tables != null) {
            this.tables = tables;
        }
        this.externalVenueId = java.util.UUID.randomUUID().toString(); // Generamos un ID externo
    }

    /**
     * Método de dominio: Actualizar información del venue.
     */
    public void updateInfo(String name, String address) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del local no puede ser nulo o vacío.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("La dirección no puede ser nula o vacía.");
        }
        this.name = name;
        this.address = address;
    }

    /**
     * Añade una nueva mesa al local, validando duplicados.
     */
    public void addTable(TableVenue table) {
        if (table == null) {
            throw new IllegalArgumentException("La mesa no puede ser nula.");
        }
        if (this.tables.contains(table)) {
            throw new IllegalArgumentException("La mesa ya existe en el local.");
        }
        this.tables.add(table);
    }

    /**
     * Método de dominio: Buscar una mesa por ID.
     */
    public Optional<TableVenue> findTableById(Long tableId) {
        if (tableId == null) {
            return Optional.empty();
        }
        return this.tables.stream()
                .filter(table -> table.getId() != null && table.getId().equals(tableId))
                .findFirst();
    }

    /**
     * Verifica si hay mesas disponibles para una capacidad requerida.
     */
    public boolean hasAvailableTablesForCapacity(int requiredCapacity) {
        return this.tables.stream()
                .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                .anyMatch(table -> table.getCapacity() != null && table.getCapacity().capacity() >= requiredCapacity);
    }

    // ...otros métodos de negocio específicos...
}
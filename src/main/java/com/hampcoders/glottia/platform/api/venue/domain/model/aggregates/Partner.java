package com.hampcoders.glottia.platform.api.venue.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venue.domain.model.entities.Venue;

/**
 * Aggregate Root para el Bounded Context Partner.
 * Toda modificación a los Venues debe pasar por el Partner.
 */

@Getter
@Setter
@Entity
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario que es el Partner (viene del IAM BC)
    private Long userId;

    private String legalName; // Nombre legal del negocio/socio

    // Relación de composición (uno a muchos) con Venue
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private final List<Venue> venues = new ArrayList<>();

    protected Partner() {
    }

    /**
     * Constructor al registrar el Partner con validaciones.
     */
    public Partner(Long userId, String legalName, List<Venue> initialVenues) {
        if (userId == null || legalName == null || legalName.trim().isEmpty()) {
            throw new IllegalArgumentException("El Partner debe tener un User ID y un Nombre Legal.");
        }
        this.userId = userId;
        this.legalName = legalName;
        if (initialVenues != null) {
            this.venues.addAll(initialVenues);
        }
    }

    public List<Venue> getVenues() {
        return new ArrayList<>(venues); // Retorna una copia defensiva
    }

    /**
     * Método de dominio: Actualizar información básica del partner.
     */
    public void updateBasicInfo(String legalName) {
        if (legalName == null || legalName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre legal no puede ser nulo o vacío.");
        }
        this.legalName = legalName;
    }

    /**
     * Método de dominio: Agregar un nuevo local al socio.
     */
    public void addVenue(Venue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("El local no puede ser nulo.");
        }
        if (this.venues.contains(venue)) {
            throw new IllegalArgumentException("El venue ya existe en el partner.");
        }
        this.venues.add(venue);
    }

    /**
     * Método de dominio: Buscar un venue por ID.
     */
    public Optional<Venue> findVenueById(Long venueId) {
        if (venueId == null) {
            return Optional.empty();
        }
        return this.venues.stream()
                .filter(venue -> venue.getId() != null && venue.getId().equals(venueId))
                .findFirst();
    }

    /**
     * Método de dominio: Remover un venue del partner por ID.
     */
    public void removeVenueById(Long venueId) {
        if (venueId == null) {
            throw new IllegalArgumentException("El ID del venue no puede ser nulo.");
        }
        boolean removed = this.venues.removeIf(venue ->
            venue.getId() != null && venue.getId().equals(venueId)
        );
        if (!removed) {
            throw new IllegalArgumentException("No se encontró el venue con ID: " + venueId);
        }
    }

    /**
     * Método de dominio: Remover un venue del partner.
     */
    public void removeVenue(Venue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("El local no puede ser nulo.");
        }
        this.venues.remove(venue);
    }
}
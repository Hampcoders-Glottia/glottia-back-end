package com.hampcoders.glottia.platform.api.venue.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.MinimumConsumption;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableCapacity;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableStatus;

/**
 * Entidad de dominio. Es parte del agregado Partner a través de Venue.
 * Representa una mesa o espacio físico que se puede reservar.
 */
@Getter
@Setter
@Entity
@Table(name = "venue_tables")
public class TableVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ej: "Mesa 5", "Cubículo A"

    @Embedded
    private TableCapacity capacity;

    @Embedded
    private MinimumConsumption minimumConsumption;

    private LocalTime startReservationTime; // Hora de inicio de la disponibilidad
    private LocalTime endReservationTime;   // Hora de fin de la disponibilidad

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.AVAILABLE;

    protected TableVenue() {
    }

    /**
     * Constructor principal con validaciones de negocio.
     */
    public TableVenue(String name, TableCapacity capacity, MinimumConsumption minimumConsumption, LocalTime startReservationTime, LocalTime endReservationTime) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la mesa no puede ser nulo o vacío.");
        }
        if (capacity == null) {
            throw new IllegalArgumentException("La capacidad no puede ser nula.");
        }
        if (minimumConsumption == null) {
            throw new IllegalArgumentException("El consumo mínimo no puede ser nulo.");
        }
        if (startReservationTime == null || endReservationTime == null || startReservationTime.isAfter(endReservationTime)) {
            throw new IllegalArgumentException("Los horarios de reserva deben ser válidos.");
        }
        this.name = name;
        this.capacity = capacity;
        this.minimumConsumption = minimumConsumption;
        this.startReservationTime = startReservationTime;
        this.endReservationTime = endReservationTime;
        this.status = TableStatus.AVAILABLE;
    }

    /**
     * Actualiza el estado de la mesa, validando reglas de negocio.
     */
    public void updateStatus(TableStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        // Ejemplo de lógica: solo se puede reservar si está disponible
        if (newStatus == TableStatus.RESERVED && this.status != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Solo se puede reservar una mesa disponible.");
        }
        this.status = newStatus;
    }


    // ...otros métodos de negocio específicos...
}

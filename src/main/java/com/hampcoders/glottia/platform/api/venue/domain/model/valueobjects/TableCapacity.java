package com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;

/**
 * Representa el Aforo (capacidad máxima) de una mesa.
 */
@Embeddable
public record TableCapacity(
        @Min(value = 4, message = "La capacidad debe ser de al menos 4.")
        int capacity
) {

    // Constructor Canónico Compacto para validaciones
    public TableCapacity {
        if (capacity <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser de al menos 1.");
        }
    }

    public TableCapacity() {
        this(4);
    }
}



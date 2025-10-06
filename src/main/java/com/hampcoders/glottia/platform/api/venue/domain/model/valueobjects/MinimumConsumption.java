package com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * Representa el Consumo Mínimo que se exige en una mesa.
 */
@Embeddable
public record MinimumConsumption(
        @NotNull(message = "El monto no puede ser nulo.")
        @DecimalMin(value = "0.0", inclusive = true, message = "El consumo mínimo no puede ser negativo.")
        Float amount,
        String currency
) {

    // Constructor Canónico Compacto para validaciones
    public MinimumConsumption {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("El consumo mínimo no puede ser negativo.");
        }
        // Aseguramos que la moneda se establezca si es nula (convención interna)
        if (currency == null || currency.isBlank()) {
            currency = "PEN";
        }
    }

    public MinimumConsumption() {
        this(0.0f, "PEN");
    }
}
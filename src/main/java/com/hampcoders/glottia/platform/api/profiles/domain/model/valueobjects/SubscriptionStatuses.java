package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

public enum SubscriptionStatuses {
    PENDING(0),    // Pendiente de aprobación
    ACTIVE(1),     // Activo - acceso completo
    SUSPENDED(2),  // Suspendido - acceso restringido temporalmente
    CANCELLED(3),  // Cancelado - suscripción terminada
    EXPIRED(4);    // Expirado - requiere renovación

    private final int value;

    SubscriptionStatuses(int value) {
        this.value = value;
    }
}
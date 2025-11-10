package com.hampcoders.glottia.platform.api.encounters.interfaces.acl;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

import java.time.LocalDateTime;

/**
 * Facade del Bounded Context de Encounters.
 * Proporciona una interfaz simplificada para que otros BCs interactúen
 * con el dominio de Encounters & Engagement.
 */
public interface EncountersContextFacade {

    /**
     * Verifica si un Learner tiene un conflicto de horario.
     * Usado por Profiles BC o el mismo Encounters BC antes de crear uno nuevo.
     * @param learnerId El ID del Learner
     * @param scheduledAt La hora del nuevo evento
     * @return true si hay un conflicto (otro encounter +/- 2 horas), false si no.
     */
    boolean hasConflictingEncounter(LearnerId learnerId, LocalDateTime scheduledAt);

    /**
     * Crea una cuenta de lealtad para un nuevo Learner.
     * Llamado por el Profiles BC cuando un usuario completa el perfil de Learner.
     * @param learnerId El ID del Learner
     * @return El ID de la cuenta de lealtad creada, o 0L si falla.
     */
    Long createLoyaltyAccount(LearnerId learnerId);

    /**
     * Publica un evento cuando un Learner se registra (Check-in).
     * Escuchado por Analytics BC.
     * (Esta es una implementación de Event Handler, pero la fachada puede exponer el resultado)
     */
    // void publishLearnerCheckedInEvent(Long encounterId, LearnerId learnerId, VenueId venueId);

    /**
     * Publica un evento cuando un Encounter se completa.
     * Escuchado por Analytics BC.
     */
    // void publishEncounterCompletedEvent(Long encounterId, List<LearnerId> attendeeIds);
    
    // ... otros métodos que Analytics o Profiles necesiten consultar.
}

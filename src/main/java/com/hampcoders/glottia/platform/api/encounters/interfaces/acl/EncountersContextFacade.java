package com.hampcoders.glottia.platform.api.encounters.interfaces.acl;

import java.time.LocalDateTime;

/**
 * Context facade for Encounters Bounded Context.
 * It provides a simplified interface for other Bounded Contexts to interact
 * with the Encounters domain.
 */
public interface EncountersContextFacade {

    /**
     * Verifies if a Learner has a scheduling conflict.
     * Used by Profiles BC or the same Encounters BC before creating a new
     * encounter.
     * 
     * @param learnerId   The Learner ID
     * @param scheduledAt The time of the new event
     * @return true if there is a conflict (another encounter +/- 2 hours), false
     *         otherwise.
     */
    boolean hasConflictingEncounter(Long learnerId, LocalDateTime scheduledAt);

    /**
     * Creates a loyalty account for a new Learner.
     * Called by the Profiles BC when a user completes the Learner profile.
     * 
     * @param learnerId The Learner ID
     * @return The ID of the loyalty account created, or 0L if it fails.
     */
    Long createLoyaltyAccount(Long learnerId);

    /**
     * Publica un evento cuando un Learner se registra (Check-in).
     * Escuchado por Analytics BC.
     * (Esta es una implementación de Event Handler, pero la fachada puede exponer
     * el resultado)
     */
    // void publishLearnerCheckedInEvent(Long encounterId, LearnerId learnerId,
    // VenueId venueId);

    /**
     * Publica un evento cuando un Encounter se completa.
     * Escuchado por Analytics BC.
     */
    // void publishEncounterCompletedEvent(Long encounterId, List<LearnerId>
    // attendeeIds);

    // ... otros métodos que Analytics o Profiles necesiten consultar.
}

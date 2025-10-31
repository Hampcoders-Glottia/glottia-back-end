package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a Learner ID.
 * @summary
 * Represents the unique identifier for a learner in the system.
 * This class is immutable and ensures that the learner ID is a positive number.
 * @param learnerId The unique identifier of the learner.
 * @see IllegalArgumentException
 */
@Embeddable
public record LearnerId(Long learnerId) {
    public LearnerId {
        if (learnerId == null || learnerId <= 0) {
            throw new IllegalArgumentException("Learner ID must be a positive number.");
        }
    }

    public LearnerId() {
        this(0L); 
    }

    public static LearnerId of(Long learnerId) {
        return new LearnerId(learnerId);
    }

    @Override
    public String toString() {
        return learnerId.toString();
    }
}
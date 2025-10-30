package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record LearnerId(Long learnerId) {
    public LearnerId {
        if (learnerId == null || learnerId <= 0) {
            throw new IllegalArgumentException("Learner ID must be a positive number.");
        }
    }
     public LearnerId() { this(0L); } 
}
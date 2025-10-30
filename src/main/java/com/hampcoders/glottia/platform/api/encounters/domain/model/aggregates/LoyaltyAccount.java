package com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class LoyaltyAccount extends AuditableAbstractAggregateRoot<LoyaltyAccount> { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "learnerId", column = @Column(name = "learner_id", unique = true))
    private LearnerId learnerId;

    private Integer points = 0;
    private Integer encountersCreated = 0;
    private Integer encountersAttended = 0;
    private Integer noShowCount = 0;
    // Podríamos agregar una lista de Badges si se manejan como entidades/VOs aquí

    protected LoyaltyAccount() {}

    public LoyaltyAccount(LearnerId learnerId) {
        this.learnerId = learnerId;
    }

    public void awardPointsForCreation() {
        this.points += 5; // Valor fijo o configurable
        this.encountersCreated++;
        // Emitir evento PointsAwardedEvent
    }

    public void awardPointsForAttendance(int pointsToAdd) {
        if (pointsToAdd <= 0) throw new IllegalArgumentException("Points to add must be positive");
        this.points += pointsToAdd;
        this.encountersAttended++;
        // Emitir evento PointsAwardedEvent
        // Verificar si se desbloquea un badge
         checkAndEmitBadgeUnlock("FLUENT_FRIEND", 100);
    }

    public void penalizeNoShow(int penalty) {
        if (penalty <= 0) throw new IllegalArgumentException("Penalty must be positive");
        this.points -= penalty;
        this.noShowCount++;
        // Emitir evento PointsPenalizedEvent
    }

     public void penalizeLateCancellation(int penalty) {
         if (penalty <= 0) throw new IllegalArgumentException("Penalty must be positive");
         this.points -= penalty;
         // Emitir evento PointsPenalizedEvent
     }

    public boolean canParticipate() {
        // Regla: Un Learner con puntos < 0 no puede participar
        return this.points >= 0;
    }

     private void checkAndEmitBadgeUnlock(String badgeName, int threshold) {
         // Lógica simplificada, podrías tener una lista de badges y sus umbrales
         if (this.points >= threshold && !hasUnlockedBadge(badgeName)) {
              // Marcar badge como desbloqueado (si se persiste)
              // Emitir evento BadgeUnlockedEvent(this.learnerId, badgeName, this.points)
         }
     }

    // Método dummy, necesitaría persistencia de badges desbloqueados
     public boolean hasUnlockedBadge(String badgeName) {
        // Implementar lógica para verificar si el badge ya fue otorgado
         return false; // Placeholder
     }
}
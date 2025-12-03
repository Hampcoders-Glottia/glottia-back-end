package com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction.TransactionType;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.VenueId;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "loyalty_accounts")
public class LoyaltyAccount extends AuditableAbstractAggregateRoot<LoyaltyAccount> { 

    @Embedded
    @AttributeOverride(name = "learnerId", column = @Column(name = "learner_id", unique = true))
    private LearnerId learnerId;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @Column(name = "encounters_created", nullable = false)
    private Integer encountersCreated = 0;  

    @Column(name = "encounters_attended", nullable = false)
    private Integer encountersAttended = 0;

    @Column(name = "no_show_count", nullable = false)
    private Integer noShowCount = 0;
    // Podríamos agregar una lista de Badges si se manejan como entidades/VOs aquí

    @OneToMany(mappedBy = "loyaltyAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<LoyaltyTransaction> transactions = new ArrayList<>();


    protected LoyaltyAccount() {}

    public LoyaltyAccount(LearnerId learnerId) {
        this.learnerId = learnerId;

        addTransaction(TransactionType.WELCOME_BONUS, 300, "Bono de bienvenida", null, null, null);
    }

    public void awardPointsForCreation(Long encounterId, VenueId venueId, String venueName) {
        int pointsToAdd = 5;
        this.points += pointsToAdd;
        this.encountersCreated++;
        addTransaction(TransactionType.CREATION, pointsToAdd, 
            "Reserva completada: " + venueName, encounterId, venueId, venueName);
    }

    public void awardPointsForAttendance(int pointsToAdd, Long encounterId, VenueId venueId, String venueName) {
        if (pointsToAdd <= 0) throw new IllegalArgumentException("Points to add must be positive");
        this.points += pointsToAdd;
        this.encountersAttended++;
        addTransaction(TransactionType.ATTENDANCE, pointsToAdd, 
            "Check-in: " + venueName, encounterId, venueId, venueName);
        checkAndEmitBadgeUnlock("FLUENT_FRIEND", 100);
    }

    public void penalizeNoShow(int penalty, Long encounterId, VenueId venueId, String venueName) {
        if (penalty <= 0) throw new IllegalArgumentException("Penalty must be positive");
        this.points -= penalty;
        this.noShowCount++;
        addTransaction(TransactionType.NO_SHOW, -penalty, 
            "No-show: " + venueName, encounterId, venueId, venueName);
    }

    public void penalizeLateCancellation(int penalty, Long encounterId, VenueId venueId, String venueName) {
        if (penalty <= 0) throw new IllegalArgumentException("Penalty must be positive");
        this.points -= penalty;
        addTransaction(TransactionType.LATE_CANCEL, -penalty, 
            "Cancelación tardía: " + venueName, encounterId, venueId, venueName);
    }

    private void addTransaction(TransactionType type, Integer points, String description, 
                                 Long encounterId, VenueId venueId, String venueName) {
        var transaction = new LoyaltyTransaction(this, type, points, description, encounterId, venueId, venueName);
        this.transactions.add(transaction);
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
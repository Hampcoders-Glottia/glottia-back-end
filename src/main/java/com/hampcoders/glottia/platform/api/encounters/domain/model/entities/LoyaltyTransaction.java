package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.VenueId;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity(name = "loyalty_transactions")
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_account_id", nullable = false)
    private LoyaltyAccount loyaltyAccount;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "encounter_id")
    private Long encounterId;

    @Embedded
    @AttributeOverride(name = "venueId", column = @Column(name = "venue_id"))
    private VenueId venueId;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected LoyaltyTransaction() {}

    public LoyaltyTransaction(LoyaltyAccount loyaltyAccount, TransactionType type, Integer points, 
                               String description, Long encounterId, VenueId venueId, String venueName) {
        this.loyaltyAccount = loyaltyAccount;
        this.type = type;
        this.points = points;
        this.description = description;
        this.encounterId = encounterId;
        this.venueId = venueId;
        this.venueName = venueName;
        this.createdAt = LocalDateTime.now();
    }

    public enum TransactionType {
        CREATION,       // +5 por crear encuentro
        ATTENDANCE,     // +10 por check-in
        WELCOME_BONUS,  // +300 bono de bienvenida
        NO_SHOW,        // -15 por no asistir
        LATE_CANCEL     // -5 por cancelar tarde
    }
}
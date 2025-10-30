package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Entity(name = "attendances")
public class Attendance extends AuditableModel { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID propio de la entidad Attendance

    @ManyToOne(fetch = FetchType.LAZY) // Evita cargar Encounter innecesariamente
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @Embedded
    @AttributeOverride(name = "learnerId", column = @Column(name = "learner_id"))
    private LearnerId learnerId;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;
    
    @Column(name = "checked_in_at", nullable = true)
    private LocalDateTime checkedInAt;

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;  
    
    private Integer pointsPenalized = 0; // Para registrar la penalización

    protected Attendance() {}

    public Attendance(Encounter encounter, LearnerId learnerId) {
        this.encounter = encounter;
        this.learnerId = learnerId;
        this.status = AttendanceStatus.RESERVED;
        this.reservedAt = LocalDateTime.now();
    }

    public void checkIn() {
        // Regla 9: Check-in solo si status es RESERVED
        if (this.status != AttendanceStatus.RESERVED) {
            throw new IllegalStateException("Attendance status must be RESERVED to check-in.");
        }
        this.status = AttendanceStatus.CHECKED_IN;
        this.checkedInAt = LocalDateTime.now();
        // La lógica de otorgar puntos debería estar en el servicio/aggregate que orquesta
    }

    public void markAsNoShow() {
        // Regla 7: Penalización solo si era RESERVED y NO hizo check-in
        if (this.status != AttendanceStatus.RESERVED) {
             // Ya está checked-in, cancelado, o ya marcado como no-show
            return; // O lanzar excepción si se quiere ser más estricto
        }
        this.status = AttendanceStatus.NO_SHOW;
        // La lógica de penalizar puntos debería estar en el servicio/aggregate
    }

    public void cancel(LocalDateTime cancellationTime, LocalDateTime encounterScheduledAt) {
         if (this.status == AttendanceStatus.CHECKED_IN || this.status == AttendanceStatus.NO_SHOW || this.status == AttendanceStatus.CANCELLED) {
             throw new IllegalStateException("Cannot cancel attendance with status: " + this.status);
         }

         long hoursBefore = ChronoUnit.HOURS.between(cancellationTime, encounterScheduledAt);
         this.status = AttendanceStatus.CANCELLED;

         // Regla 11: Penalización si cancela < 24h antes
         if (hoursBefore < 24) {
             // La lógica de penalizar está en LoyaltyAccount, aquí solo marcamos el estado
             // Se podría añadir un flag o devolver un valor para indicar la penalización
         }
         // Emitir AttendanceCancelledEvent
    }

     public boolean requiresLateCancellationPenalty(LocalDateTime cancellationTime, LocalDateTime encounterScheduledAt) {
         return this.status == AttendanceStatus.RESERVED &&
                ChronoUnit.HOURS.between(cancellationTime, encounterScheduledAt) < 24;
     }

}
package com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity(name = "encounters")
public class Encounter extends AuditableAbstractAggregateRoot<Encounter> {

    @Embedded
    @AttributeOverride(name = "learnerId", column = @Column(name = "creator_id"))
    private LearnerId creatorId;

    @Embedded
    @AttributeOverride(name = "venueId", column = @Column(name = "venue_id"))
    private VenueId venueId;

    @Embedded
    @AttributeOverride(name = "tableId", column = @Column(name = "table_id"))
    private TableId tableId;

    @Column(name = "topic", nullable = false)
    private String topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", referencedColumnName = "id", nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cefr_level_id", referencedColumnName = "id", nullable = false)
    private CEFRLevel level;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "encounter_status_id", referencedColumnName = "id", nullable = false)
    private EncounterStatus status;

    @Column(name = "min_capacity", nullable = false)
    private final Integer minCapacity = 4;

    @Column(name = "max_capacity", nullable = false)
    private final Integer maxCapacity = 6;

    @Embedded
    private AttendanceList attendances;

    protected Encounter() {
        this.attendances = new AttendanceList();
        this.status = EncounterStatus.DRAFT;
    }

    public Encounter(CreateEncounterCommand command) {
        this();
        if (command.scheduledAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("ScheduledAt cannot be in the past.");
        }
        this.creatorId = command.creatorId(); // Asignar creatorId del comando
        this.venueId = command.venueId();     // Asignar venueId del comando
        // tableId se asignará después, tras la confirmación del Venue BC
        this.topic = command.topic();
        this.language = command.language();
        this.level = command.level();
        this.scheduledAt = command.scheduledAt();
        // El creador se une automáticamente
        this.join(this.creatorId);
    }

     public void assignTable(TableId tableId) {
        if (this.status != EncounterStatus.DRAFT) {
             throw new IllegalStateException("Cannot assign table if status is not DRAFT");
        }
        this.tableId = tableId;
     }

    public void join(LearnerId learnerId) {
        if (this.status != EncounterStatus.PUBLISHED && this.status != EncounterStatus.READY) {
            throw new IllegalStateException("Cannot join encounter if it's not PUBLISHED or READY");
        }
        if (attendances.getConfirmedCount() >= maxCapacity) {
            throw new IllegalStateException("Encounter is full");
        }
        // Valida si el learner ya está (regla 15)
        if (attendances.hasLearner(learnerId)) {
             throw new IllegalStateException("Learner already joined this encounter");
        }
        this.attendances.addItem(this, learnerId);
        // Podrías emitir evento LearnerJoinedEncounterEvent aquí si usas eventos de dominio
        if (attendances.getConfirmedCount() >= minCapacity && this.status == EncounterStatus.PUBLISHED) {
             this.status = EncounterStatus.READY;
             // Podrías emitir evento EncounterReadyEvent aquí
        }
         if (attendances.getConfirmedCount() == maxCapacity) {
             // Podrías emitir evento EncounterCapacityReachedEvent aquí
         }
    }

    public void checkIn(LearnerId learnerId) {
        if (!isInCheckInWindow()) {
            throw new IllegalStateException("Check-in is only allowed 1 hour before to 15 minutes after the start time.");
        }
        if (this.status != EncounterStatus.READY && this.status != EncounterStatus.IN_PROGRESS) {
             throw new IllegalStateException("Cannot check-in if encounter is not READY or IN_PROGRESS");
        }
        Attendance attendance = attendances.findByLearnerId(learnerId)
                .orElseThrow(() -> new IllegalArgumentException("Learner not found in this encounter"));
        attendance.checkIn(); // Valida estado interno en Attendance
         // Podrías emitir evento LearnerCheckedInEvent aquí
    }

    public void start() {
        if (this.status != EncounterStatus.READY) {
            throw new IllegalStateException("Encounter must be in READY state to start.");
        }
        // Regla 2: No iniciar sin mínimo 4 check-ins
        if (attendances.getCheckedInCount() < minCapacity) {
             throw new IllegalStateException("Cannot start encounter with less than " + minCapacity + " checked-in learners.");
        }
        this.status = EncounterStatus.IN_PROGRESS;
        // Podrías emitir evento EncounterStartedEvent aquí
    }

     public void complete() {
         if (this.status != EncounterStatus.IN_PROGRESS) {
             throw new IllegalStateException("Encounter must be IN_PROGRESS to be completed.");
         }
         this.status = EncounterStatus.COMPLETED;
         // Marcar No-Shows
         attendances.markNoShows();
         // Podrías emitir evento EncounterCompletedEvent aquí
     }

     public void cancel(String reason) {
          // Regla 4: No cancelar si hay check-ins
          if (attendances.getCheckedInCount() > 0 && this.status == EncounterStatus.IN_PROGRESS) {
              throw new IllegalStateException("Cannot cancel encounter with checked-in learners while IN_PROGRESS.");
          }
           if (this.status == EncounterStatus.COMPLETED || this.status == EncounterStatus.CANCELLED) {
                throw new IllegalStateException("Encounter is already COMPLETED or CANCELLED.");
           }
          this.status = EncounterStatus.CANCELLED;
          // Cancelar todas las attendances RESERVED
          attendances.cancelAllReserved(LocalDateTime.now(), this.scheduledAt);
          // Podrías emitir evento EncounterCancelledEvent aquí, incluyendo la razón
     }

      public void publish() {
          if (this.status != EncounterStatus.DRAFT) {
              throw new IllegalStateException("Encounter must be in DRAFT state to be published.");
          }
           if (this.tableId == null) {
                throw new IllegalStateException("Cannot publish encounter without a confirmed table.");
           }
          this.status = EncounterStatus.PUBLISHED;
           // Podrías emitir evento EncounterPublishedEvent aquí
      }


     // --- Métodos de validación ---

    public boolean isInCheckInWindow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = this.scheduledAt.minusHours(1);
        LocalDateTime windowEnd = this.scheduledAt.plusMinutes(15);
        return !now.isBefore(windowStart) && !now.isAfter(windowEnd);
    }

     public boolean needsAutoCancellation() {
         return (this.status == EncounterStatus.READY || this.status == EncounterStatus.PUBLISHED) &&
                LocalDateTime.now().isAfter(this.scheduledAt.minusMinutes(30)) &&
                attendances.getCheckedInCount() < minCapacity;
     }

    public boolean canStart() {
        return this.status == EncounterStatus.READY && attendances.getCheckedInCount() >= minCapacity;
    }
}
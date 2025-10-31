package com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

import com.hampcoders.glottia.platform.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


/**
 * Encounters aggregate root.
 * @summary
 * This class represents an Encounter aggregate root in the Glottia platform.
 * It contains attributes and behaviors related to language learning encounters,
 * including scheduling, status management, attendance tracking, and learner interactions.
 * It extends AuditableAbstractAggregateRoot to inherit auditing properties.
 */
@Getter
@Entity
@Table(name = "encounters")
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
        this.status = EncounterStatus.getDefaultEncounterStatus();
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
        this.language = new Language(Languages.valueOf(command.language()));
        this.level = new CEFRLevel(CEFRLevels.valueOf(command.cefrLevel()));
        this.scheduledAt = command.scheduledAt();
        // El creador se une automáticamente
        this.join(this.creatorId);
    }

    // TODO: Implement updateEncounter method

    public void assignTable(TableId tableId) {
        if (this.status.getName() != EncounterStatuses.DRAFT) {
            throw new IllegalStateException("Cannot assign table if status is not DRAFT");
        }
        this.tableId = tableId;
    }

    /**
     * Learner joins the encounter.
     * @throws IllegalStateException if the encounter is not PUBLISHED or READY,
     * @param learnerId
    */
    public void join(LearnerId learnerId) {
        if (!this.status.isPublished() && !this.status.isReady()) {
            throw new IllegalStateException("Cannot join encounter if it's not PUBLISHED or READY");
        }
        if (attendances.getConfirmedCount() >= maxCapacity) {
            throw new IllegalStateException("Encounter is full");
        }
        if (attendances.hasLearner(learnerId)) {
            throw new IllegalStateException("Learner already joined this encounter");
        }
        
        this.attendances.addItem(this, learnerId);
        
        if (attendances.getConfirmedCount() >= minCapacity && this.status.isPublished()) {
            transitionTo(EncounterStatuses.READY);
        }
    }

    /**
     * Learner checks in to the encounter.
     * @param learnerId
     * @throws IllegalStateException if not in check-in window or encounter not READY/IN
     */
    public void checkIn(LearnerId learnerId) {
        if (!isInCheckInWindow()) {
            throw new IllegalStateException("Check-in is only allowed 1 hour before to 15 minutes after the start time.");
        }
        if (this.status.getName() != EncounterStatuses.READY && this.status.getName() != EncounterStatuses.IN_PROGRESS) {
             throw new IllegalStateException("Cannot check-in if encounter is not READY or IN_PROGRESS");
        }
        Attendance attendance = attendances.findByLearnerId(learnerId)
                .orElseThrow(() -> new IllegalArgumentException("Learner not found in this encounter"));
        attendance.checkIn(); // Valida estado interno en Attendance
         // Podrías emitir evento LearnerCheckedInEvent aquí
    }

    /**
     * Start the encounter, transitioning its status from READY to IN_PROGRESS.
     * @throws IllegalStateException if the encounter is not in READY state or if there are not enough checked-in learners.
     */
    public void start() {
        if (!this.status.isReady()) {
            throw new IllegalStateException("Encounter must be in READY state to start.");
        }
        if (attendances.getCheckedInCount() < minCapacity) {
            throw new IllegalStateException("Cannot start encounter with less than " + minCapacity + " checked-in learners.");
        }
        transitionTo(EncounterStatuses.IN_PROGRESS);
        // TODO: Emit event EncounterStartedEvent here
    }

    /**
     * Complete the encounter, transitioning its status from IN_PROGRESS to COMPLETED.
     * @throws IllegalStateException if the encounter is not in IN_PROGRESS state.
     */
    public void complete() {
        if (this.status.getName() != EncounterStatuses.IN_PROGRESS) {
            throw new IllegalStateException("Encounter must be IN_PROGRESS to be completed.");
        }
        transitionTo(EncounterStatuses.COMPLETED);
        attendances.markNoShows();
        // Podrías emitir evento EncounterCompletedEvent aquí
    }

    /**
     * Cancel the encounter, transitioning its status to CANCELLED.
     * @param reason Reason for cancellation.
     * @throws IllegalStateException if there are checked-in learners while IN_PROGRESS or if already
     */
    public void cancel(String reason) {
        if (attendances.getCheckedInCount() > 0 && this.status.getName() == EncounterStatuses.IN_PROGRESS) {
            throw new IllegalStateException("Cannot cancel encounter with checked-in learners while IN_PROGRESS.");
        }
        if (this.status.getName() == EncounterStatuses.COMPLETED || this.status.getName() == EncounterStatuses.CANCELLED) {
            throw new IllegalStateException("Encounter is already COMPLETED or CANCELLED.");
        }
        transitionTo(EncounterStatuses.CANCELLED);
        attendances.cancelAllReserved(LocalDateTime.now(), this.scheduledAt);
          // Podrías emitir evento EncounterCancelledEvent aquí, incluyendo la razón
     }

    /**
     * Publish the encounter, transitioning its status from DRAFT to PUBLISHED.
    * @throws IllegalStateException if the encounter is not in DRAFT state or if no
    * @return table is assigned.
    */
    public void publish() {
        if (!this.status.isDraft())
            throw new IllegalStateException("Encounter must be in DRAFT state to be published.");
        
        if (this.tableId == null) 
                throw new IllegalStateException("Cannot publish encounter without a confirmed table.");
        
        transitionTo(EncounterStatuses.PUBLISHED);
        // TODO: Emit event EncounterPublishedEvent aquí
    }


     // --- Métodos de validación ---
    public boolean isInCheckInWindow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = this.scheduledAt.minusHours(1);
        LocalDateTime windowEnd = this.scheduledAt.plusMinutes(15);
        return !now.isBefore(windowStart) && !now.isAfter(windowEnd);
    }

    public boolean needsAutoCancellation() {
         return (this.status.getName() == EncounterStatuses.READY || this.status.getName() == EncounterStatuses.PUBLISHED) &&
                LocalDateTime.now().isAfter(this.scheduledAt.minusMinutes(30)) &&
                attendances.getCheckedInCount() < minCapacity;
    }

    public boolean canStart() {
        return this.status.getName() == EncounterStatuses.READY && attendances.getCheckedInCount() >= minCapacity;
    }

    /**
     * Transition the encounter to a new status.
     * @param newStatus
     */
    private void transitionTo(EncounterStatuses newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Cannot transition from " + this.status.getName() + " to " + newStatus);
        }
        this.status = new EncounterStatus(newStatus);
    }
}
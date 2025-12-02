package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Attendance entity.
 * 
 * @summary
 *          This class represents the attendance of a learner to an encounter.
 *          It tracks the status of the attendance (reserved, checked-in,
 *          no-show, cancelled),
 *          the timestamps for reservation and check-in, and points awarded or
 *          penalized.
 *          It extends AuditableModel to include createdAt and updatedAt
 *          timestamps.
 */
@Getter
@Entity(name = "attendances")
public class Attendance extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the encounter this attendance belongs to.
     * Lazy loading to avoid unnecessary queries.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    /**
     * Learner ID (Value Object from Profiles BC).
     */
    @Embedded
    @AttributeOverride(name = "learnerId", column = @Column(name = "learner_id", nullable = false))
    private LearnerId learnerId;

    /**
     * Current attendance status (catalog entity).
     */
    @ManyToOne(fetch = FetchType.EAGER) // Eager porque casi siempre necesitamos el status
    @JoinColumn(name = "attendance_status_id", nullable = false)
    private AttendanceStatus status;

    /**
     * Timestamp when the learner reserved their spot.
     */
    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    /**
     * Timestamp when the learner checked in (null if not checked in).
     */
    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    /**
     * Points awarded for this attendance (e.g., +10 for check-in).
     */
    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;

    /**
     * Points penalized for this attendance (e.g., -15 for no-show, -5 for late
     * cancel).
     */
    @Column(name = "points_penalized", nullable = false)
    private Integer pointsPenalized = 0;

    /**
     * JPA constructor (protected).
     */
    protected Attendance() {
    }

    /**
     * Create a new Attendance with RESERVED status.
     * 
     * @param encounter The encounter this attendance belongs to
     * @param learnerId The learner's ID
     */
    public Attendance(Encounter encounter, LearnerId learnerId, AttendanceStatus status) {
        if (encounter == null) {
            throw new IllegalArgumentException("Encounter cannot be null");
        }
        if (learnerId == null) {
            throw new IllegalArgumentException("LearnerId cannot be null");
        }

        if (status == null)
            throw new IllegalArgumentException("Status cannot be null"); // Fail fast

        this.encounter = encounter;
        this.learnerId = learnerId;
        this.status = status; // ASSIGN MANAGED ENTITY

        this.reservedAt = LocalDateTime.now();
        this.pointsAwarded = 0;
        this.pointsPenalized = 0;
    }

    // ==================== BUSINESS LOGIC (State Transitions) ====================

    /**
     * Mark attendance as checked-in.
     * <p>
     * <strong>Business Rule:</strong> Can only check-in if status is RESERVED.
     * </p>
     * 
     * @throws IllegalStateException if status is not RESERVED
     */
    public void checkIn(AttendanceStatus checkedInStatus) {
        if (!this.status.isReserved()) {
            throw new IllegalStateException("Can only check-in from RESERVED status");
        }
        if (this.checkedInAt != null) {
            throw new IllegalStateException("Already checked in");
        }

        // Transition to CHECKED_IN
        this.status = checkedInStatus;
        this.checkedInAt = LocalDateTime.now();

        // Note: Points are awarded by the service layer (LoyaltyAccount)
        // This method only handles state transition
    }

    /**
     * Mark attendance as NO_SHOW.
     * <p>
     * <strong>Business Rule:</strong> Can only mark as no-show if status is
     * RESERVED.
     * </p>
     * <p>
     * Called when encounter completes and learner didn't check in.
     * </p>
     * 
     * @throws IllegalStateException if status is not RESERVED
     */
    public void markAsNoShow() {
        if (!this.status.isReserved()) {
            // Already checked-in, cancelled, or already marked as no-show
            throw new IllegalStateException(
                    "Cannot mark as NO_SHOW. Current status is " + this.status.getStringName());
        }

        // Transition to NO_SHOW
        transitionTo(AttendanceStatuses.NO_SHOW);

        // Note: Penalty is applied by the service layer (LoyaltyAccount)
    }

    /**
     * Cancel attendance.
     * <p>
     * <strong>Business Rules:</strong>
     * </p>
     * <ul>
     * <li>Cannot cancel if already CHECKED_IN, NO_SHOW, or CANCELLED</li>
     * <li>Late cancellation (< 24h before encounter) triggers penalty</li>
     * </ul>
     * 
     * @param cancellationTime     When the cancellation occurs
     * @param encounterScheduledAt When the encounter is scheduled
     * @throws IllegalStateException if status doesn't allow cancellation
     */
    public void cancel(LocalDateTime cancellationTime, LocalDateTime encounterScheduledAt) {
        if (cancellationTime == null || encounterScheduledAt == null) {
            throw new IllegalArgumentException("Cancellation time and encounter scheduled time cannot be null");
        }

        // Validate current status allows cancellation
        if (this.status.isCheckedIn()) {
            throw new IllegalStateException("Cannot cancel attendance after check-in");
        }
        if (this.status.isNoShow()) {
            throw new IllegalStateException("Cannot cancel attendance marked as NO_SHOW");
        }
        if (this.status.isCancelled()) {
            throw new IllegalStateException("Attendance is already cancelled");
        }

        // Transition to CANCELLED
        transitionTo(AttendanceStatuses.CANCELLED);

        // Note: Penalty calculation is done by the service layer
        // This method only handles state transition
    }

    /**
     * Record points awarded (called by service layer after check-in).
     * 
     * @param points Points to award (must be positive)
     */
    public void recordPointsAwarded(Integer points) {
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points awarded must be non-negative");
        }
        this.pointsAwarded = points;
    }

    /**
     * Record points penalized (called by service layer after no-show or late
     * cancel).
     * 
     * @param points Points to penalize (must be positive, will be stored as
     *               positive value)
     */
    public void recordPointsPenalized(Integer points) {
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points penalized must be non-negative");
        }
        this.pointsPenalized = points;
    }

    // ==================== QUERY METHODS (No Mutation) ====================

    /**
     * Check if this attendance requires late cancellation penalty.
     * <p>
     * <strong>Business Rule:</strong> Penalty if cancelled < 24 hours before
     * encounter.
     * </p>
     * 
     * @param cancellationTime     When the cancellation occurred
     * @param encounterScheduledAt When the encounter is scheduled
     * @return true if penalty should be applied
     */
    public boolean requiresLateCancellationPenalty(LocalDateTime cancellationTime, LocalDateTime encounterScheduledAt) {
        if (cancellationTime == null || encounterScheduledAt == null) {
            return false;
        }

        // Only applies if status is CANCELLED and was previously RESERVED
        if (!this.status.isCancelled()) {
            return false;
        }

        long hoursBefore = ChronoUnit.HOURS.between(cancellationTime, encounterScheduledAt);
        return hoursBefore < 24;
    }

    /**
     * Check if this attendance is active (RESERVED or CHECKED_IN).
     * 
     * @return true if active
     */
    public boolean isActive() {
        return this.status.isActive();
    }

    /**
     * Check if learner has checked in.
     * 
     * @return true if status is CHECKED_IN
     */
    public boolean hasCheckedIn() {
        return this.status.isCheckedIn();
    }

    /**
     * Get the status name as enum.
     * 
     * @return AttendanceStatuses enum
     */
    public AttendanceStatuses getStatusName() {
        return this.status.getName();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Internal method to transition to a new status.
     * Validates transition and creates new AttendanceStatus instance.
     * 
     * @param newStatus Target status
     * @throws IllegalStateException if transition is not allowed
     */
    private void transitionTo(AttendanceStatuses newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s",
                            this.status.getStringName(), newStatus.name()));
        }

        // Create new status instance (immutability principle)
        this.status = new AttendanceStatus(newStatus);
    }

}
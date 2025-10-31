package com.hampcoders.glottia.platform.api.encounters.domain.model.entities;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * AttendanceStatus Entity
 * Catalog entity representing an attendance status.
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_statuses")
public class AttendanceStatus {

    /**
     * Unique identifier for the AttendanceStatus.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the AttendanceStatus.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private AttendanceStatuses name;    

    /**
     * Constructor to create AttendanceStatus from AttendanceStatuses enum.
     * @param name The AttendanceStatuses enum value.
     * @return AttendanceStatus instance.
     */
    public AttendanceStatus(AttendanceStatuses name) {
        this.name = name;
    }

    /**
     * Get the string name of the AttendanceStatus.
     * @return String name
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the integer value of the AttendanceStatus.
     * @return int value
     */
    public int getNameValue() {
        return name.getValue();
    }

    /**
     * Check if status is RESERVED.
     * 
     * @return true if status is RESERVED
     */
    public boolean isReserved() {
        return AttendanceStatuses.RESERVED.equals(this.name);
    }

    /**
     * Check if status is CHECKED_IN.
     * 
     * @return true if status is CHECKED_IN
     */
    public boolean isCheckedIn() {
        return AttendanceStatuses.CHECKED_IN.equals(this.name);
    }

    /**
     * Check if status is NO_SHOW.
     * 
     * @return true if status is NO_SHOW
     */
    public boolean isNoShow() {
        return AttendanceStatuses.NO_SHOW.equals(this.name);
    }

    /**
     * Check if status is CANCELLED.
     * 
     * @return true if status is CANCELLED
     */
    public boolean isCancelled() {
        return AttendanceStatuses.CANCELLED.equals(this.name);
    }

    /**
     * Check if status is active (RESERVED or CHECKED_IN).
     * 
     * @return true if status is active
     */
    public boolean isActive() {
        return isReserved() || isCheckedIn();
    }

    /**
     * Validate if transition to new status is allowed.
     * 
     * @param newStatus Target status
     * @return true if transition is valid
     */
    public boolean canTransitionTo(AttendanceStatuses newStatus) {
        if (newStatus == null) {
            return false;
        }

        return switch (this.name) {
            case RESERVED -> newStatus == AttendanceStatuses.CHECKED_IN ||
                           newStatus == AttendanceStatuses.NO_SHOW ||
                           newStatus == AttendanceStatuses.CANCELLED;
            
            case CHECKED_IN -> false; 
            case NO_SHOW -> false; 
            case CANCELLED -> false; 
        };
    }

    /**
     * Get the default AttendanceStatus entity (RESERVED).
     * @return AttendanceStatus instance
     */
    public static AttendanceStatus getDefaultAttendanceStatus() {
        return new AttendanceStatus(AttendanceStatuses.RESERVED);
    }

    /**
     * Convert from String name to AttendanceStatus entity.
     * @param name 
     * @return AttendanceStatus instance
     */
    public static AttendanceStatus toAttendanceStatusFromStringName(String name) {
        return new AttendanceStatus(AttendanceStatuses.valueOf(name));
    }

    /**
     * Convert from Long to AttendanceStatus entity.
     * @param id
     * @return AttendanceStatus instance
     */
    public static AttendanceStatus toAttendanceStatusFromId(Long newAttendanceStatusId) {
        return new AttendanceStatus(AttendanceStatuses.fromValue(newAttendanceStatusId.intValue()));
    }
}

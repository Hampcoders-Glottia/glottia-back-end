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
     * Create AttendanceStatus from integer value.
     * @param value
     * @return AttendanceStatus instance
     */
    public static AttendanceStatus fromValue(int value) {
        return new AttendanceStatus(AttendanceStatuses.fromValue(value));
    }
}

package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.LocalDate;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.EncounterId;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

/**
 * AvailabilityCalendar Entity
 * Represents a daily availability slot for a table
 * Owned by Table (composition relationship)
 */
@Entity
@Getter
@jakarta.persistence.Table(name = "availability_calendars", 
       uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"table_id", "availability_date"}))
public class AvailabilityCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(name = "availability_date", nullable = false)
    private LocalDate availabilityDate;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved;

    @Embedded
    private EncounterId encounterId;

    // Constructor
    protected AvailabilityCalendar() {
        super();
        this.isAvailable = true;
        this.isReserved = false;
    }

    protected AvailabilityCalendar(Table table, LocalDate availabilityDate) {
        this();
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        if (availabilityDate == null) {
            throw new IllegalArgumentException("Availability date cannot be null");
        }
        if (availabilityDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot create calendar for past dates");
        }

        this.table = table;
        this.availabilityDate = availabilityDate;
    }

    // Business methods
    public void reserve() {
        if (isReserved) {
            throw new IllegalStateException("Slot is already reserved");
        }
        if (!isAvailable) {
            throw new IllegalStateException("Slot is not available");
        }
        if (isPastDate()) {
            throw new IllegalStateException("Cannot reserve past dates");
        }

        this.isReserved = true;
        this.isAvailable = false;
    }

    public void release() {
        if (!isReserved) {
            throw new IllegalStateException("Slot is not reserved");
        }

        this.isReserved = false;
        this.isAvailable = true;
        this.encounterId = null;
    }

    public void markUnavailable() {
        if (isReserved) {
            throw new IllegalStateException("Cannot mark as unavailable while reserved");
        }
        this.isAvailable = false;
    }

    public void markAvailable() {
        if (isReserved) {
            throw new IllegalStateException("Cannot mark as available while reserved");
        }
        this.isAvailable = true;
    }

    public void setEncounterId(EncounterId encounterId) {
        if (!isReserved) {
            throw new IllegalStateException("Cannot set encounter ID on non-reserved slot");
        }
        this.encounterId = encounterId;
    }
    public boolean isReserved() {
        return Boolean.TRUE.equals(this.isReserved);
    }

    public boolean isPastDate() {
        return availabilityDate.isBefore(LocalDate.now());
    }

    public boolean isFutureDate() {
        return availabilityDate.isAfter(LocalDate.now());
    }

    public boolean isToday() {
        return availabilityDate.equals(LocalDate.now());
    }
}
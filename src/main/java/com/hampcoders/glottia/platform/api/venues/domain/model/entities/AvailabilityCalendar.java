package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.EncounterId;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

/**
 * AvailabilityCalendar Entity
 * Represents a time-based availability slot for a table
 * Supports both specific date slots and recurring weekly patterns
 * Owned by Table (composition relationship)
 */
@Entity
@Getter
@jakarta.persistence.Table(name = "availability_calendars")
public class AvailabilityCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(name = "availability_date")
    private LocalDate availabilityDate; // Nullable - for recurring patterns

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 10)
    private DayOfWeek dayOfWeek; // Nullable - for specific dates

    @Column(name = "start_hour", nullable = false)
    private LocalTime startHour;

    @Column(name = "end_hour", nullable = false)
    private LocalTime endHour;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved;

    @Embedded
    private EncounterId encounterId;

    // Constructors
    protected AvailabilityCalendar() {
        super();
        this.isAvailable = true;
        this.isReserved = false;
    }

    // Constructor for specific date slots
    protected AvailabilityCalendar(Table table, LocalDate availabilityDate, LocalTime startHour, LocalTime endHour) {
        this();
        validateTable(table);
        validateSpecificDate(availabilityDate);
        validateTimeRange(startHour, endHour);

        this.table = table;
        this.availabilityDate = availabilityDate;
        this.dayOfWeek = null;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    // Constructor for recurring pattern slots
    protected AvailabilityCalendar(Table table, DayOfWeek dayOfWeek, LocalTime startHour, LocalTime endHour) {
        this();
        validateTable(table);
        validateDayOfWeek(dayOfWeek);
        validateTimeRange(startHour, endHour);

        this.table = table;
        this.availabilityDate = null;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    // Validation methods
    private void validateTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
    }

    private void validateSpecificDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Availability date cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot create calendar for past dates");
        }
    }

    private void validateDayOfWeek(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
    }

    private void validateTimeRange(LocalTime startHour, LocalTime endHour) {
        if (startHour == null) {
            throw new IllegalArgumentException("Start hour cannot be null");
        }
        if (endHour == null) {
            throw new IllegalArgumentException("End hour cannot be null");
        }
        if (!endHour.isAfter(startHour)) {
            throw new IllegalArgumentException("End hour must be after start hour");
        }

        // Validate 2-hour duration requirement (1.5h encounter + 0.5h cleanup)
        long durationMinutes = Duration.between(startHour, endHour).toMinutes();
        if (durationMinutes != 120) {
            throw new IllegalArgumentException(
                    "Time slot must be exactly 2 hours (120 minutes). Found: " + durationMinutes + " minutes");
        }
    }

    // Helper methods
    public boolean isRecurringPattern() {
        return dayOfWeek != null && availabilityDate == null;
    }

    public boolean isSpecificDate() {
        return availabilityDate != null && dayOfWeek == null;
    }

    public Duration getTimeSlotDuration() {
        return Duration.between(startHour, endHour);
    }

    public boolean overlapsWithTimeSlot(LocalTime otherStart, LocalTime otherEnd) {
        return startHour.isBefore(otherEnd) && endHour.isAfter(otherStart);
    }

    // Business methods
    public void reserve() {
        if (isReserved) {
            throw new IllegalStateException("Slot is already reserved");
        }
        if (!isAvailable) {
            throw new IllegalStateException("Slot is not available");
        }
        if (isPastTime()) {
            throw new IllegalStateException("Cannot reserve past time slots");
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

    public boolean isPastTime() {
        // For recurring patterns, only check at reservation time
        if (isRecurringPattern()) {
            return false;
        }

        // For specific dates, check if the end time has passed
        LocalDateTime slotEndTime = LocalDateTime.of(availabilityDate, endHour);
        return slotEndTime.isBefore(LocalDateTime.now());
    }

    public boolean isPastDate() {
        if (availabilityDate == null) {
            return false; // Recurring patterns don't have past dates
        }
        return availabilityDate.isBefore(LocalDate.now());
    }

    public boolean isFutureDate() {
        if (availabilityDate == null) {
            return false; // Recurring patterns are timeless
        }
        return availabilityDate.isAfter(LocalDate.now());
    }

    public boolean isToday() {
        if (availabilityDate == null) {
            return false;
        }
        return availabilityDate.equals(LocalDate.now());
    }
}
package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.EncounterId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityCalendarRepository extends JpaRepository<AvailabilityCalendar, Long> {

    // Existing methods (updated for new schema)
    Optional<AvailabilityCalendar> findByTableIdAndAvailabilityDate(Long tableId, LocalDate availabilityDate);

    List<AvailabilityCalendar> findByTableIdAndAvailabilityDateBetween(Long tableId, LocalDate fromDate,
            LocalDate toDate);

    List<AvailabilityCalendar> findByTableId(Long tableId);

    List<AvailabilityCalendar> findByEncounterId(EncounterId encounterId);

    boolean existsByTableIdAndAvailabilityDate(Long tableId, LocalDate availabilityDate);

    boolean existsByEncounterId(EncounterId encounterId);

    // New methods for time-based slots

    // Find specific slot by all parameters
    Optional<AvailabilityCalendar> findByTableAndAvailabilityDateAndStartHourAndEndHour(
            Table table, LocalDate date, LocalTime startHour, LocalTime endHour);

    // Find all slots for a table on a specific date
    List<AvailabilityCalendar> findByTableAndAvailabilityDate(Table table, LocalDate date);

    // Find recurring pattern for a table and day of week
    List<AvailabilityCalendar> findByTableAndDayOfWeek(Table table, DayOfWeek dayOfWeek);

    // Find recurring pattern by specific time slot
    Optional<AvailabilityCalendar> findByTableAndDayOfWeekAndStartHourAndEndHour(
            Table table, DayOfWeek dayOfWeek, LocalTime startHour, LocalTime endHour);

    // Check for overlapping time slots on a specific date
    @Query("SELECT ac FROM AvailabilityCalendar ac WHERE ac.table = :table " +
            "AND ac.availabilityDate = :date " +
            "AND ((ac.startHour < :endHour AND ac.endHour > :startHour))")
    List<AvailabilityCalendar> findOverlappingSlotsByDate(
            @Param("table") Table table,
            @Param("date") LocalDate date,
            @Param("startHour") LocalTime startHour,
            @Param("endHour") LocalTime endHour);

    // Check for overlapping time slots in recurring pattern
    @Query("SELECT ac FROM AvailabilityCalendar ac WHERE ac.table = :table " +
            "AND ac.dayOfWeek = :dayOfWeek " +
            "AND ((ac.startHour < :endHour AND ac.endHour > :startHour))")
    List<AvailabilityCalendar> findOverlappingSlotsByDayOfWeek(
            @Param("table") Table table,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startHour") LocalTime startHour,
            @Param("endHour") LocalTime endHour);

    // Find available slots for a table on a specific date (combining specific +
    // recurring)
    @Query("SELECT ac FROM AvailabilityCalendar ac WHERE ac.table = :table " +
            "AND (ac.availabilityDate = :date OR ac.dayOfWeek = :dayOfWeek) " +
            "AND ac.isAvailable = true AND ac.isReserved = false")
    List<AvailabilityCalendar> findAvailableSlots(
            @Param("table") Table table,
            @Param("date") LocalDate date,
            @Param("dayOfWeek") DayOfWeek dayOfWeek);

    // Find all specific date slots for a table
    @Query("SELECT ac FROM AvailabilityCalendar ac WHERE ac.table = :table " +
            "AND ac.availabilityDate IS NOT NULL")
    List<AvailabilityCalendar> findSpecificDateSlots(@Param("table") Table table);

    // Find all recurring pattern slots for a table
    @Query("SELECT ac FROM AvailabilityCalendar ac WHERE ac.table = :table " +
            "AND ac.dayOfWeek IS NOT NULL")
    List<AvailabilityCalendar> findRecurringPatternSlots(@Param("table") Table table);
}
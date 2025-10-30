package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.EncounterId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityCalendarRepository extends JpaRepository<AvailabilityCalendar, Long> {
    Optional<AvailabilityCalendar> findByTableIdAndAvailabilityDate(Long tableId, LocalDate availabilityDate);    
    List<AvailabilityCalendar> findByTableIdAndAvailabilityDateBetween(Long tableId, LocalDate fromDate, LocalDate toDate);
    List<AvailabilityCalendar> findByTableId(Long tableId);
    List<AvailabilityCalendar> findByEncounterId(EncounterId encounterId);
    boolean existsByTableIdAndAvailabilityDate(Long tableId, LocalDate availabilityDate);
    boolean existsByEncounterId(EncounterId encounterId);
}
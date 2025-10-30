package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableRegistryRepository extends JpaRepository<TableRegistry, Long> {
    Optional<TableRegistry> findByVenueId(Long venueId);

    @Query("SELECT DISTINCT tr FROM TableRegistry tr JOIN tr.tableList.tables t JOIN t.availabilityCalendars ac " + "WHERE ac.availabilityDate = :date AND ac.isAvailable = true")
    List<TableRegistry> findAllWithAvailableTablesOnDate(@Param("date") LocalDate date);
}
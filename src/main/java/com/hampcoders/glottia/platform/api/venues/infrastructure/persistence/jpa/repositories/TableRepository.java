package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    @Query("SELECT t FROM Table t WHERE t.tableRegistry.venue.id = :venueId")
    List<Table> findByVenueId(@Param("venueId") Long venueId);
    List<Table> findByTableTypeId(Long tableTypeId);
    List<Table> findByTableStatusId(Long tableStatusId);
    
    @Query("SELECT count(t) FROM Table t WHERE t.tableRegistry.venue.id = :venueId")
    long countByVenueId(@Param("venueId") Long venueId);
}

package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerVenueRegistryRepository extends JpaRepository<PartnerVenueRegistry, Long> {
    Optional<PartnerVenueRegistry> findByPartnerId(PartnerId partnerId);
    boolean existsByPartnerId(PartnerId partnerId);

    @Query("SELECT DISTINCT p FROM PartnerVenueRegistry p JOIN p.venueList.registrations r JOIN r.venue v WHERE v.isActive = true")
    List<PartnerVenueRegistry> findAllWithActiveVenues();

    @Query("SELECT vr FROM VenueRegistration vr " + "WHERE vr.venue.id = :venueId " + "AND vr.isActive = true")
    Optional<VenueRegistration> findActiveRegistrationByVenueId(@Param("venueId") Long venueId);

}
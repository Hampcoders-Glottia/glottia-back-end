package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.PartnerVenueRegistry;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRegistrationRepository extends JpaRepository<VenueRegistration, Long> {
    @Query("SELECT vr FROM VenueRegistration vr WHERE vr.partnerVenueRegistry.partnerId = :partnerId")
    List<VenueRegistration> findByPartnerId(@Param("partnerId") PartnerId partnerId);

    @Query("SELECT vr FROM VenueRegistration vr WHERE vr.partnerVenueRegistry.partnerId = :partnerId AND vr.venue.isActive = true")
    List<VenueRegistration> findActiveByPartnerId(@Param("partnerId") PartnerId partnerId);

    @Query("SELECT vr FROM VenueRegistration vr WHERE vr.partnerVenueRegistry.partnerId = :partnerId AND vr.venue.isActive = :isActive")
    List<VenueRegistration> findByPartnerIdAndIsActive(@Param("partnerId") PartnerId partnerId, @Param("isActive") Boolean isActive);
    boolean existsByPartnerVenueRegistryAndVenue(PartnerVenueRegistry registry, Venue venue);
}
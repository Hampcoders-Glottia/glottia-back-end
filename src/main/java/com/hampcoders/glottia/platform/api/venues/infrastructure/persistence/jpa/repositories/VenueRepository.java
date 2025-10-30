package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.Address;


@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
    List<Venue> findByAddressCity(String city);
    List<Venue> findByAddress(Address address);
    List<Venue> findByVenueTypeId(Long venueTypeId);
    List<Venue> findByIsActiveTrue(); // Para GetActiveVenuesQuery
    List<Venue> findByIsActiveFalse(); // Para GetInactiveVenuesQuery
    @Query("SELECT DISTINCT v FROM Venue v JOIN v.promotionList.promotionItems vp WHERE vp.isActive = true")
    List<Venue> findAllWithActivePromotions(); // Para GetVenuesWithActivePromotionsQuery
    boolean existsByName(String name);
    boolean existsByAddress(Address address);
    boolean existsByNameAndIdIsNot(String name, Long id);
    boolean existsByAddressAndIdIsNot(Address address, Long id);
    
    @Query("SELECT vr.venue FROM PartnerVenueRegistry pvr JOIN pvr.venueList.registrations vr WHERE pvr.partnerId = :partnerId")
    List<Venue> findByPartnerId(@Param("partnerId") PartnerId partnerId);
}
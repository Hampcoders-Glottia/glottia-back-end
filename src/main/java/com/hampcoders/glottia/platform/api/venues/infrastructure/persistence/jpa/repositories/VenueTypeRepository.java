package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.VenueTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueTypeRepository extends JpaRepository<VenueType, Long> {
    boolean existsByName(VenueTypes name);
    Optional<VenueType> findByName(VenueTypes name);
}
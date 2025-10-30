package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;

@Repository
public interface TableTypeRepository extends JpaRepository<TableType, Long> {
    boolean existsByName(TableTypes name);
    Optional<TableType> findByName(TableTypes name);
}
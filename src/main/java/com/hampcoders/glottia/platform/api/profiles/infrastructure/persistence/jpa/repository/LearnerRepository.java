package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;

import java.util.List;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, Long> {
    List<Learner> findByAddressCountry(String country);
}
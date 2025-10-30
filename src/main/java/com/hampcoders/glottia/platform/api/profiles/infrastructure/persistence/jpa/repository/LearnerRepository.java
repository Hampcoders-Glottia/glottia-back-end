package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Learner;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, Long> {
    List<Learner> findByCityIgnoreCase(String city);
    List<Learner> findByCountryIgnoreCase(String country);
    
    @Query("SELECT DISTINCT l FROM Learner l JOIN l.learnerLanguages.learnerLanguageItems lli WHERE lli.language.id = :languageId")
    List<Learner> findByLearnerLanguageItemsLanguageId(@Param("languageId") Long languageId);
}
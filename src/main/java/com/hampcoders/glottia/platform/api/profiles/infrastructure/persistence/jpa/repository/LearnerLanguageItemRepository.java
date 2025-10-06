package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.LearnerLanguageItem;

@Repository
public interface LearnerLanguageItemRepository extends JpaRepository<LearnerLanguageItem, Long> {
    List<LearnerLanguageItem> findByLearnerId(Long learnerId);
    List<LearnerLanguageItem> findByLearnerProfileId(Long profileId);
    List<LearnerLanguageItem> findByLanguageId(Long languageId);
    List<LearnerLanguageItem> findByLearnerIdAndIsLearning(Long learnerId, boolean isLearning);
}
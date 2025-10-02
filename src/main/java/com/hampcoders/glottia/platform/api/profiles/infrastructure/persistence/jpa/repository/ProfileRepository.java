package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.NivelCefr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByFullName(String fullName);
    boolean existsByFullNameAndIdIsNot(String fullName, Long id);
    Optional<Profile> findByFullName(String fullName);
    List<Profile> findByLevel(NivelCefr level);
    List<Profile> findByLanguage(Languages language);
    List<Profile> findByAge(int age);
}

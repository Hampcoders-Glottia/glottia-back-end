package com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE CONCAT(p.firstName, ' ', p.lastName) = :fullName")
    boolean existsByFullName(@Param("fullName") String fullName);
    
    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE CONCAT(p.firstName, ' ', p.lastName) = :fullName AND p.id != :id")
    boolean existsByFullNameAndIdIsNot(@Param("fullName") String fullName, @Param("id") Long id);
    
    @Query("SELECT p FROM Profile p WHERE CONCAT(p.firstName, ' ', p.lastName) = :fullName")
    Optional<Profile> findByFullName(@Param("fullName") String fullName);
    
    Optional<Profile> findByEmail(String email);
    List<Profile> findByAge(int age);
    List<Profile> findByBusinessRole(BusinessRoles businessRole);
    Optional<Profile> findByUserId(String userId);
}

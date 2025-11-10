package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Attendance entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom methods to find attendances by encounter ID, learner ID, and status.
 * Supports pagination and sorting.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEncounterId(Long encounterId);
    List<Attendance> findByLearnerId(LearnerId learnerId);
    List<Attendance> findByLearnerIdAndStatus(LearnerId learnerId, AttendanceStatus status);
}

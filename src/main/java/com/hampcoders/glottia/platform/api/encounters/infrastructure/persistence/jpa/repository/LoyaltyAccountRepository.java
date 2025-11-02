package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, Long> {
    Optional<LoyaltyAccount> findByLearnerId(LearnerId learnerId);
    boolean existsByLearnerId(LearnerId learnerId);

    // Para el Leaderboard
    List<LoyaltyAccount> findTopNByOrderByPointsDesc(Pageable pageable);
}

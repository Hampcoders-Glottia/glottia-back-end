package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {

    @Query("SELECT t FROM loyalty_transactions t WHERE t.loyaltyAccount.learnerId = :learnerId ORDER BY t.createdAt DESC")
    Page<LoyaltyTransaction> findByLearnerIdOrderByCreatedAtDesc(LearnerId learnerId, Pageable pageable);
}
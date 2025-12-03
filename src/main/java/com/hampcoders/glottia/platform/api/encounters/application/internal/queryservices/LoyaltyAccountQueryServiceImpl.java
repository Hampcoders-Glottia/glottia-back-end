package com.hampcoders.glottia.platform.api.encounters.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.CanParticipateQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLeaderboardQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLoyaltyAccountByLearnerQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetPointsHistoryQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetUnlockedBadgesQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountQueryService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.LoyaltyAccountRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.LoyaltyTransactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoyaltyAccountQueryServiceImpl implements LoyaltyAccountQueryService {

    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    public LoyaltyAccountQueryServiceImpl(LoyaltyAccountRepository loyaltyAccountRepository,
                                          LoyaltyTransactionRepository loyaltyTransactionRepository) {
        this.loyaltyAccountRepository = loyaltyAccountRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
    }

    @Override
    public Optional<LoyaltyAccount> handle(GetLoyaltyAccountByLearnerQuery query) {
        return loyaltyAccountRepository.findByLearnerId(query.learnerId());
    }

    @Override
    public List<Object> handle(GetLeaderboardQuery query) {
        Pageable topN = PageRequest.of(0, query.topN());
        // Devolvemos los agregados, la capa de interfaz los transformará a DTOs
        return List.copyOf(loyaltyAccountRepository.findTopNByOrderByPointsDesc(topN));
    }

    @Override
    public List<Object> handle(GetUnlockedBadgesQuery query) {
        // La lógica de Badges está mockeada en el agregado, así que devolvemos vacío
        return List.of();
    }

    @Override
    public boolean handle(CanParticipateQuery query) {
        return loyaltyAccountRepository.findByLearnerId(query.learnerId())
                .map(LoyaltyAccount::canParticipate)
                .orElse(true); // Si no tiene cuenta, puede participar (se le creará una)
    }

    @Override
    public Page<LoyaltyTransaction> handle(GetPointsHistoryQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        return loyaltyTransactionRepository.findByLearnerIdOrderByCreatedAtDesc(query.learnerId(), pageable);
    }
}
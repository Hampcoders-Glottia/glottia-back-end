package com.hampcoders.glottia.platform.api.encounters.domain.services;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.LoyaltyTransaction;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface LoyaltyAccountQueryService {
    Optional<LoyaltyAccount> handle(GetLoyaltyAccountByLearnerQuery query);
    List<Object> handle(GetLeaderboardQuery query);
    List<Object> handle(GetUnlockedBadgesQuery query);
    boolean handle(CanParticipateQuery query); // Devuelve boolean
    Page<LoyaltyTransaction> handle(GetPointsHistoryQuery query);
}

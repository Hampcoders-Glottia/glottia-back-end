package com.hampcoders.glottia.platform.api.encounters.domain.services;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.*;
import java.util.Optional;


public interface LoyaltyAccountCommandService {
    Optional<LoyaltyAccount> handle(CreateLoyaltyAccountCommand command); // Devuelve la cuenta creada
    Optional<LoyaltyAccount> handle(AwardPointsCommand command);
    Optional<LoyaltyAccount> handle(PenalizeCommand command);
}
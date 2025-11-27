package com.hampcoders.glottia.platform.api.encounters.application.acl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.hampcoders.glottia.platform.api.encounters.application.internal.queryservices.LoyaltyAccountQueryServiceImpl;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateLoyaltyAccountCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLoyaltyAccountByLearnerQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.HasConflictingEncounterQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.acl.EncountersContextFacade;

@Service
public class EncountersContextFacadeImpl implements EncountersContextFacade {

    private final LoyaltyAccountQueryServiceImpl loyaltyAccountQueryServiceImpl;
    private final LoyaltyAccountCommandService loyaltyAccountCommandService;
    private final EncounterQueryService encounterQueryService;

    public EncountersContextFacadeImpl(
            LoyaltyAccountCommandService loyaltyAccountCommandService,
            EncounterQueryService encounterQueryService,
            LoyaltyAccountQueryServiceImpl loyaltyAccountQueryServiceImpl) {
        this.loyaltyAccountCommandService = loyaltyAccountCommandService;
        this.encounterQueryService = encounterQueryService;
        this.loyaltyAccountQueryServiceImpl = loyaltyAccountQueryServiceImpl;
    }

    @Override
    public boolean hasConflictingEncounter(Long learnerId, LocalDateTime scheduledAt) {
        try {
            var query = new HasConflictingEncounterQuery(
                    new LearnerId(learnerId),
                    scheduledAt);
            return encounterQueryService.handle(query);
        } catch (Exception e) {
            return false; // Default to no conflict on error
        }
    }

    @Override
    public Long createLoyaltyAccount(Long learnerId) {
        var command = new CreateLoyaltyAccountCommand(new LearnerId(learnerId));
        return loyaltyAccountCommandService.handle(command);
    }

    @Override
    public boolean existsLoyaltyAccountByLearnerId(Long learnerId) {
        try {
            var query = new GetLoyaltyAccountByLearnerQuery(new LearnerId(learnerId));
            return loyaltyAccountQueryServiceImpl.handle(query).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

}

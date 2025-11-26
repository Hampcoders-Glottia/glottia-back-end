package com.hampcoders.glottia.platform.api.encounters.application.acl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateLoyaltyAccountCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.acl.EncountersContextFacade;

@Service
public class EncountersContextFacadeImpl implements EncountersContextFacade {

    private final LoyaltyAccountCommandService loyaltyAccountCommandService;

    public EncountersContextFacadeImpl(LoyaltyAccountCommandService loyaltyAccountCommandService) {
        this.loyaltyAccountCommandService = loyaltyAccountCommandService;
    }

    @Override
    public boolean hasConflictingEncounter(Long learnerId, LocalDateTime scheduledAt) {
        return true;
    }

    @Override
    public Long createLoyaltyAccount(Long learnerId) {
        var command = new CreateLoyaltyAccountCommand(new LearnerId(learnerId));
        return loyaltyAccountCommandService.handle(command);
    }

}

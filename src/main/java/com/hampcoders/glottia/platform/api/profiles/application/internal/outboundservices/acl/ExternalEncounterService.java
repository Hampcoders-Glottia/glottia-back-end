package com.hampcoders.glottia.platform.api.profiles.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.encounters.interfaces.acl.EncountersContextFacade;

@Service
public class ExternalEncounterService {
    private final EncountersContextFacade encountersContextFacade;

    public ExternalEncounterService(EncountersContextFacade encountersContextFacade) {
        this.encountersContextFacade = encountersContextFacade;
    }

    public void createLoyaltyAccountForLearner(Long learnerId) {
        if (!encountersContextFacade.existsLoyaltyAccountByLearnerId(learnerId)) {
            encountersContextFacade.createLoyaltyAccount(learnerId);
        }
    }
}
